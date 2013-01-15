package com.ybcx.comic.tools;

import java.io.IOException;

public class TestFfmpeg {
	
	public static void main(String[] args) {
		String ffmpegPath = "D:\\test\\ffmpeg\\bin\\ffmpeg.exe";
		
		try {
			
			Process p =exec(ffmpegPath + " -r 4 -f image2 -i D:\\test\\image\\img%d.png -s 320x240 -aspect 4:3 -y D:\\test\\output.flv",false);
			System.out.println(p.waitFor());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * wrapper for Runtime.exec. No input/output. Optionally wait for child to
	 * finish.
	 * 
	 * @param command
	 *            fully qualified *.exe or *.com command
	 * @param wait
	 *            true if you want to wait for the child to finish.
	 */
	public static Process exec(String command, boolean wait) {
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			return null;
		}
		if (wait) {
			try {
				p.waitFor();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		// You must close these even if you never use them!
		try {
			p.getInputStream().close();
			p.getOutputStream().close();
			p.getErrorStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}

}
