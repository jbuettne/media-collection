package com.mediacollector.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;

/**
 * Ändert die Größe (Maße) eines gegebenen Bildes. Dieses wird im Standard-
 * Ordner auf der SD-Card des Gerätes unter dem gegeben Dateinamen gespeichert.
 * @author Philipp Dermitzel
 */
public class ImageResizer {

	/**
	 * Erstellt eine neue Grafik mit der Breite ODER Höhe [xxx]px. Dabei wird 
	 * das Seitenverhältnis des Bildes beibehalten, die größere Seite auf 
	 * [xxx]px verkleinert und die kleinere angepasst.
	 * @param originalBM Das Bitmap, welches verkleinert werden soll.
	 * @param newWidth Die neue Breite in Pixeln.
	 * @param newHeight Die neue Breite in Pixeln.
	 * @param filename Der neue Dateiname.
	 * @throws IOException .
	 */
	public ImageResizer(final Bitmap originalBM, final int newWidth, 
			final int newHeight, final String filename) 
	throws IOException {
		this(originalBM, newWidth, newHeight, filename, true);
	}
	
	/**
	 * Erstellt eine neue Grafik mit der Breite UND/ODER Höhe [xxx]px. Falls 
	 * keepAspectRatio mit 'true' übergeben wird, wird das Seitenverhältnis des
	 * Bildes beibehalten, die größere Seite auf [xxx]px verkleinert und die 
	 * kleinere angepasst.
	 * @param originalBM Bitmap Das Bitmap, welches verkleinert werden soll.
	 * @param newWidth int Die neue Breite in Pixeln.
	 * @param newHeight int Die neue Breite in Pixeln.
	 * @param filename String Der neue Dateiname.
	 * @param keepAspectRatio boolean true für das Beibehalten des Seiten-
	 * 	verhältnis, false für eine Verkleinerung des Bildes auf genau die 
	 * 	angegebenen Werte für Breite und Höhe.
	 * @throws IOException .
	 */
	public ImageResizer(final Bitmap originalBM, final int newWidth, 
			final int newHeight, final String filename, 
			final boolean keepAspectRatio) 
	throws IOException {
		int originalWidth = originalBM.getWidth();
		int originalHeight = originalBM.getHeight();
		float scaleWidth;
		float scaleHeight;
		if (keepAspectRatio) {
			if (originalWidth >= originalHeight) {
				scaleWidth = ((float) newWidth) / originalWidth;
				scaleHeight = scaleWidth;
			} else {
				scaleHeight = ((float) newHeight) / originalHeight;
				scaleWidth = scaleHeight;
			}
		} else {
			scaleWidth = ((float) newWidth) / originalWidth;
			scaleHeight = ((float) newHeight) / originalHeight;
		}
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);        
        Bitmap resizedBM = Bitmap.createBitmap(originalBM, 0, 0, originalWidth,
        		originalHeight, matrix, true);
        File newBMFile = new File (Environment.getExternalStorageDirectory() 
        	+ "/MediaCollector/" + filename);
        newBMFile.createNewFile();
        FileOutputStream out = new FileOutputStream(newBMFile);
        resizedBM.compress(Bitmap.CompressFormat.JPEG, 90, out);
	}

}
