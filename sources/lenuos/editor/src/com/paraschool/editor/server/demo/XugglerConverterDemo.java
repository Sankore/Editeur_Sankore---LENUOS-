package com.paraschool.editor.server.demo;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import com.xuggle.xuggler.Converter;

/*
 * Created at 28 août 2010
 * By bathily
 */
public class XugglerConverterDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Converter converter = new Converter();
		try
		{
			// first define options
			Options options = converter.defineOptions();
			// And then parse them.
			// -vpreset hq
			String cmd = "-vcodec libx264 -vpreset /usr/local/xuggler/share/ffmpeg/libx264-normal.ffpreset /Users/bathily/Desktop/test.mov /Users/bathily/Desktop/test.mp4";
			//String cmd = "--help";
			
			CommandLine cmdLine = converter.parseOptions(options, cmd.split(" "));
			// Finally, run the converter.
			long begin = System.currentTimeMillis();
			converter.run(cmdLine);
			long end = System.currentTimeMillis();
			System.out.printf("End in %d\n", end-begin);
		}
		catch (Exception exception)
		{
			System.err.printf("Error: %s\n", exception.getMessage());
		}
	}

}
