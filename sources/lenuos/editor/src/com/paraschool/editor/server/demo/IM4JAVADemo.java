package com.paraschool.editor.server.demo;

import java.io.IOException;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.IdentifyCmd;
import org.im4java.process.ArrayListOutputConsumer;

public class IM4JAVADemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// create command
		ConvertCmd cmd = new ConvertCmd();
		// create the operation, add images and operators/options
		IMOperation op = new IMOperation();
		op.addImage("/Users/dbathily/Desktop/Page1_v2.jpg");
		
		op.thumbnail(82,82,"^").gravity("center").extent(82, 82);
		
		
		IMOperation corner = new IMOperation();
		corner.p_clone().alpha("extract").draw("fill black polygon 0,0 0,6 6,0 fill white circle 6,6 6,0");
		corner.addSubOperation(new IMOperation().p_clone().flip()).compose("Multiply").composite();
		corner.addSubOperation(new IMOperation().p_clone().flop()).compose("Multiply").composite();
		
		op.addSubOperation(corner);
		
		op.alpha("off").compose("CopyOpacity").composite();
		
		op.addImage("/Users/dbathily/Desktop/Page1Thumbnail.png");

		System.out.println(op.toString());
		
		IdentifyCmd icmd = new IdentifyCmd();
		IMOperation iop = new IMOperation();
		iop.addImage("/Users/dbathily/Desktop/Page1_v2.jpg");
		
		ArrayListOutputConsumer outputConsumer = new ArrayListOutputConsumer(); 
		icmd.setOutputConsumer(outputConsumer);
		
		try {
			cmd.run(op);
			icmd.run(iop);
			String identity = outputConsumer.getOutput().get(0);
			String[] fields = identity.split(" ");
			for(int i=0 ; i< fields.length ; i++) {
				System.out.println(fields[i]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IM4JavaException e) {
			e.printStackTrace();
		}

	}

}
