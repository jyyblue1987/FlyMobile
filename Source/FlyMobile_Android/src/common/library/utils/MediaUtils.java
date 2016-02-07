package common.library.utils;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;

public class MediaUtils {
	public static void playAudio(String path)
	{
		if( CheckUtils.isEmpty(path) )
			return;
		
	    MediaPlayer mp = new MediaPlayer();
	    try {
			mp.setDataSource(path);
			mp.prepare();
			mp.start();
			mp.setVolume(10, 10);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
	}
	
	public static void playAudioWithIntent(Context context, String path)
	{
		Intent intent = new Intent();  
		intent.setAction(android.content.Intent.ACTION_VIEW);  
		File file = new File(path);  
		intent.setDataAndType(Uri.fromFile(file), "audio/*");  
		context.startActivity(intent);
	}
}
