/*
 * Copyright 2001-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.apache.jmeter.save;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.swing.JComponent;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import org.apache.batik.ext.awt.image.codec.PNGEncodeParam;
import org.apache.batik.ext.awt.image.codec.PNGImageEncoder;
import org.apache.batik.ext.awt.image.codec.tiff.TIFFEncodeParam;
import org.apache.batik.ext.awt.image.codec.tiff.TIFFImageEncoder;

/**
 * Class is responsible for taking a component and saving it as a JPEG, PNG or
 * TIFF. The class is very simple. Thanks to Batik and the developers who worked
 * so hard on it. The original implementation I used JAI, which allows
 * redistribution but requires indemnification. Luckily Batik has an alternative
 * to JAI. Hurray for Apache projects. I don't see any noticeable differences
 * between Batik and JAI.
 */
public class SaveGraphicsService {

	public static final int PNG = 0;

	public static final int TIFF = 1;

	public static final String PNG_EXTENSION = ".png";

	public static final String TIFF_EXTENSION = ".tif";

	public static final String JPEG_EXTENSION = ".jpg";

	/**
	 * 
	 */
	public SaveGraphicsService() {
		super();
	}

	/**
	 * If someone wants to save a JPEG, use this method. There is a limitation
	 * though. It uses gray scale instead of color due to artifacts with color
	 * encoding. For some reason, it does not translate pure red and orange
	 * correctly. To make the text readable, gray scale is used.
	 * 
	 * @param filename
	 * @param component
	 */
	public void saveUsingJPEGEncoder(String filename, JComponent component) {
		Dimension size = component.getSize();
		// We use Gray scale, since color produces poor quality
		// this is an unfortunate result of the default codec
		// implementation.
		BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_USHORT_GRAY);
		Graphics2D grp = image.createGraphics();
		component.paint(grp);

		File outfile = new File(filename + JPEG_EXTENSION);
		FileOutputStream fos = createFile(outfile);
		JPEGEncodeParam param = JPEGCodec.getDefaultJPEGEncodeParam(image);
		Float q = new Float(1.0);
		param.setQuality(q.floatValue(), true);
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos, param);

		try {
			encoder.encode(image);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Method will save the JComponent as an image. The formats are PNG, and
	 * TIFF.
	 * 
	 * @param filename
	 * @param type
	 * @param component
	 */
	public void saveJComponent(String filename, int type, JComponent component) {
		Dimension size = component.getSize();
		BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D grp = image.createGraphics();
		component.paint(grp);

		if (type == PNG) {
			filename += PNG_EXTENSION;
			this.savePNGWithBatik(filename, image);
		} else if (type == TIFF) {
			filename = filename + TIFF_EXTENSION;
			this.saveTIFFWithBatik(filename, image);
		}
	}

	/**
	 * Use Batik to save a PNG of the graph
	 * 
	 * @param filename
	 * @param image
	 */
	public void savePNGWithBatik(String filename, BufferedImage image) {
		File outfile = new File(filename);
		OutputStream fos = createFile(outfile);
		PNGEncodeParam param = PNGEncodeParam.getDefaultEncodeParam(image);
		PNGImageEncoder encoder = new PNGImageEncoder(fos, param);
		try {
			encoder.encode(image);
		} catch (Exception e) {
			// do nothing
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
				// do nothing
			}
		}
	}

	/**
	 * Use Batik to save a TIFF file of the graph
	 * 
	 * @param filename
	 * @param image
	 */
	public void saveTIFFWithBatik(String filename, BufferedImage image) {
		File outfile = new File(filename);
		OutputStream fos = createFile(outfile);
		TIFFEncodeParam param = new TIFFEncodeParam();
		TIFFImageEncoder encoder = new TIFFImageEncoder(fos, param);
		try {
			encoder.encode(image);
		} catch (Exception e) {
			// do nothing
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
				// do nothing
			}
		}
	}

	/**
	 * Create a new file for the graphics. Since the method creates a new file,
	 * we shouldn't get a FNFE.
	 * 
	 * @param filename
	 * @return
	 */
	public FileOutputStream createFile(File filename) {
		try {
			return new FileOutputStream(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

}
