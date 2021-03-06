package org.rpi.songcast.core;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import org.apache.log4j.Logger;

public class SongcastPlayerJavaSound implements ISongcastPlayer {

	private static SongcastPlayerJavaSound instance = null;

	private Logger log = Logger.getLogger(this.getClass());

	private AudioFormat audioFormat = new AudioFormat(44100, 16, 2, true, true);
	private DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat, 16000);

	private SourceDataLine soundLine = null;

	private boolean bWrite = false;

	public static SongcastPlayerJavaSound getInstance() {
		if (instance == null) {
			instance = new SongcastPlayerJavaSound();
		}
		return instance;
	}

	private SongcastPlayerJavaSound() {
		createSoundLine();
	}

	public void createSoundLine() {
		try {
			if (soundLine == null) {
				soundLine = (SourceDataLine) AudioSystem.getLine(info);
				soundLine.open(audioFormat);
				soundLine.start();
				bWrite = true;
			}
		} catch (Exception e) {
			log.error("Error opening Sound:", e);
		}
	}

	@Override
	public void play() {

	}

	@Override
	public void stop() {
		try {
			soundLine.close();
			soundLine = null;
			bWrite = false;
			instance = null;
		} catch (Exception e) {
			log.error("Error Closing Stream", e);
		}

	}

	public void addData(byte[] data) {
		if (!bWrite)
			return;
		try {
			if (soundLine != null) {
				// log.debug(data.length);
				soundLine.write(data, 0, data.length);
			}
		} catch (Exception e) {
			log.error("Error Writing Data", e);
		}
	}

}
