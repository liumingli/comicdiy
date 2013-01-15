package com.ybcx.comic.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;

/**
 * 图片转换<br>
 * <br>
 * 这里的图片转换是 利用 java 调用外部程序 ffmpeg 进行处理.<br>
 * ffmpeg.exe能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）<br>
 * 对ffmpeg.exe无法解析的文件格式(wmv9，rm，rmvb等),<br>
 * 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式；<br>
 * mencoder.exe;<br>
 * drv43260.dll;<br>
 * pncrt.dll<br>
 * 以上这3个文件是为文件格式(wmv9，rm，rmvb等）<br>
 * 转换为avi(ffmpeg能解析的)格式准备的；再把转换好的avi文件再用ffmpeg.exe转换成flv格式的视频文件<br>
 * <br>
 * 
 * @author huayi<br>
 * 
 */
public class ImageConvertor {

	public static void main(String[] args) {
		
		String ffmpegPath = "D:\\test\\ffmpeg\\bin\\ffmpeg.exe";
		
		String imageSource = "D:\\test\\image\\img%d.png";
		String videoSource = "D:\\test\\output.flv";
		converToVideo(ffmpegPath,imageSource,videoSource);

	}
	
	private static void converToVideo(String ffmpegPath, String imageSource,
			String videoSource) {

		List<String> cmdParam = new LinkedList<String>();
		cmdParam.add(ffmpegPath);
		cmdParam.add("-y");
		cmdParam.add("-i");
		cmdParam.add(imageSource);
//		cmdParam.add("-ab");
//		cmdParam.add("56");
//		cmdParam.add("-ar");
//		cmdParam.add("22050");
//		cmdParam.add("-b");
//		cmdParam.add("500");
		cmdParam.add("-s");
		cmdParam.add("320*240");
		cmdParam.add(videoSource);

		execCmd(cmdParam);
	}


	/**
	 * 
	 * @param cmd
	 */
	private static void execCmd(List<String> cmd) {
		final ProcessBuilder pb = new ProcessBuilder();

		// 每个进程都有标准的输入、输出、和错误流。(stdin ,stdout ,stderr)
		// 合并子进程的 【错误流】和 常规的 【输出流】
		pb.redirectErrorStream(true);
		pb.command(cmd);

		try {
			final Process p = pb.start();

			OutputStream os = p.getOutputStream();
			InputStream in = p.getInputStream();
			// 开启单独的线程来处理输入和输出流，避免缓冲区满导致线程阻塞.
			new Thread(new Receiver(in)).start();
			new Thread(new Sender(os)).start();

			try {
				p.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
				// 唤醒当前线程
				Thread.currentThread().interrupt();
			}

			System.out.println("Child done");
			// at this point the child is complete. All of its output may or may
			// not have been processed however.
			// The Receiver thread will continue until it has finished
			// processing it.
			// You must close the streams even if you never use them! In this
			// case the threads close is and os.
			p.getErrorStream().close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}


final class Sender implements Runnable {
	private static final String lineSeparator = System
			.getProperty("line.separator");
	private final OutputStream os;

	public void run() {
		try {
			final BufferedWriter bw = new BufferedWriter(
					new OutputStreamWriter(os), 500);
			for (int i = 99; i >= 0; i--) {
				bw.write("There are " + i + " bottles of beer on the wall, "
						+ i + " bottles of beer.");
				bw.write(lineSeparator);
			}
			bw.close();
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"IOException sending data to child process.");
		}
	}

	Sender(OutputStream os) {
		this.os = os;
	}
}

final class Receiver implements Runnable {
	private final InputStream is;

	public void run() {
		try {
			final BufferedReader br = new BufferedReader(new InputStreamReader(
					is), 500);
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			br.close();
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"IOException receiving data from child process.");
		}
	}

	Receiver(InputStream is) {
		this.is = is;
	}
}
