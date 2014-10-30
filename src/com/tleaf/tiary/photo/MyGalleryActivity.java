package com.tleaf.tiary.photo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tleaf.tiary.Common;
import com.tleaf.tiary.R;
import com.tleaf.tiary.adapter.GalleryAdapter;
import com.tleaf.tiary.model.MyGallery;
import com.tleaf.tiary.util.Util;

import eu.janmuller.android.simplecropimage.CropImage;



public class MyGalleryActivity extends Activity {

	public static final String TAG = "MyGalleryActivity";


	private File mFileTemp;

	GridView gridGallery;
	Handler handler;
	GalleryAdapter adapter;

	ImageView imgNoMedia;
	Button btnGalleryOk;

	String action;
	private ImageLoader imageLoader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery);

		action = getIntent().getAction();
		if (action == null) {
			finish();
		}
		initImageLoader();
		init();
	}

	private void initImageLoader() {
		try {
			String CACHE_DIR = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/.temp_tmp";
			new File(CACHE_DIR).mkdirs();

			File cacheDir = StorageUtils.getOwnCacheDirectory(getBaseContext(),
					CACHE_DIR);

			DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
			.cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
			.bitmapConfig(Bitmap.Config.RGB_565).build();
			ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
					getBaseContext())
			.defaultDisplayImageOptions(defaultOptions)
			.discCache(new UnlimitedDiscCache(cacheDir))
			.memoryCache(new WeakMemoryCache());

			ImageLoaderConfiguration config = builder.build();
			imageLoader = ImageLoader.getInstance();
			imageLoader.init(config);

		} catch (Exception e) {

		}
	}

	private void init() {

		TextView txt_album = (TextView) findViewById(R.id.txt_album);
		TextView txt_camera = (TextView) findViewById(R.id.txt_camera);

		txt_album.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openGallery();
			}
		});

		txt_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				takePicture();
			}
		});

		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mFileTemp = new File(Environment.getExternalStorageDirectory(), Common.TEMP_PHOTO_FILE_NAME);
		}
		else {
			mFileTemp = new File(getFilesDir(), Common.TEMP_PHOTO_FILE_NAME);
		}

		handler = new Handler();
		gridGallery = (GridView) findViewById(R.id.gridGallery);
		gridGallery.setFastScrollEnabled(true);
		adapter = new GalleryAdapter(getApplicationContext(), imageLoader);
		PauseOnScrollListener listener = new PauseOnScrollListener(imageLoader,
				true, true);
		gridGallery.setOnScrollListener(listener);

		if (action.equalsIgnoreCase(Common.ACTION_MULTIPLE_PICK)) {

			findViewById(R.id.llBottomContainer).setVisibility(View.VISIBLE);
			gridGallery.setOnItemClickListener(mItemMulClickListener);
			adapter.setMultiplePick(true);

		} 
		//		else if (action.equalsIgnoreCase(Action.ACTION_PICK)) {
		//
		//			findViewById(R.id.llBottomContainer).setVisibility(View.GONE);
		//			gridGallery.setOnItemClickListener(mItemSingleClickListener);
		//			adapter.setMultiplePick(false);
		//
		//		}

		gridGallery.setAdapter(adapter);
		imgNoMedia = (ImageView) findViewById(R.id.imgNoMedia);

		btnGalleryOk = (Button) findViewById(R.id.btnGalleryOk);
		btnGalleryOk.setOnClickListener(mOkClickListener);

		new Thread() {

			@Override
			public void run() {
				Looper.prepare();
				handler.post(new Runnable() {

					@Override
					public void run() {
						adapter.addAll(getGalleryPhotos());
						checkImageStatus();
					}
				});
				Looper.loop();
			};

		}.start();

	}

	private void takePicture() {

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		try {
			Uri mImageCaptureUri = null;
			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				mImageCaptureUri = Uri.fromFile(mFileTemp);
			}
			else {
				/*
				 * The solution is taken from here: http://stackoverflow.com/questions/10042695/how-to-get-camera-result-as-a-uri-in-data-folder
				 */
				mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
			}	
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, Common.REQUEST_CODE_TAKE_PICTURE);
		} catch (ActivityNotFoundException e) {

			Log.d(TAG, "cannot take picture", e);
		}
	}

	private void openGallery() {

		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, Common.REQUEST_CODE_GALLERY);
	}
	
    private void startCropImage() {

        Intent intent = new Intent(this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
        intent.putExtra(CropImage.SCALE, true);

        intent.putExtra(CropImage.ASPECT_X, 3);
        intent.putExtra(CropImage.ASPECT_Y, 2);

        startActivityForResult(intent, Common.REQUEST_CODE_CROP_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }

        Bitmap bitmap;

        switch (requestCode) {

            case Common.REQUEST_CODE_GALLERY:

                try {

                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();

                    startCropImage();

                } catch (Exception e) {

                    Log.e(TAG, "Error while creating temp file", e);
                }

                break;
            case Common.REQUEST_CODE_TAKE_PICTURE:

                startCropImage();
                break;
            case Common.REQUEST_CODE_CROP_IMAGE:

                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                if (path == null) {
                    return;
                }

//                bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
                //이미지를 넘긴다
//                mImageView.setImageBitmap(bitmap);
                
        		Util.ll("mFileTemp.getPath()", mFileTemp.getPath());
            	Intent cropedData = new Intent();
            	cropedData.putExtra("type", "crop");
            	cropedData.putExtra("path", mFileTemp.getPath());
    			setResult(RESULT_OK, cropedData);
    			finish();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }


	private void checkImageStatus() {
		if (adapter.isEmpty()) {
			imgNoMedia.setVisibility(View.VISIBLE);
		} else {
			imgNoMedia.setVisibility(View.GONE);
		}
	}

	View.OnClickListener mOkClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			ArrayList<MyGallery> selected = adapter.getSelected();

			String[] allPath = new String[selected.size()];
			for (int i = 0; i < allPath.length; i++) {
				allPath[i] = selected.get(i).sdcardPath;
			}

			Intent data = new Intent();
			data.putExtra("all_path", allPath);
			data.putExtra("type", "allPath");
			setResult(RESULT_OK, data);
			finish();

		}
	};
	AdapterView.OnItemClickListener mItemMulClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> l, View v, int position, long id) {
			adapter.changeSelection(v, position);

		}
	};

//	AdapterView.OnItemClickListener mItemSingleClickListener = new AdapterView.OnItemClickListener() {
//
//		@Override
//		public void onItemClick(AdapterView<?> l, View v, int position, long id) {
//			MyGallery item = adapter.getItem(position);
//			Intent data = new Intent().putExtra("single_path", item.sdcardPath);
//			setResult(RESULT_OK, data);
//			finish();
//		}
//	};

	private ArrayList<MyGallery> getGalleryPhotos() {
		ArrayList<MyGallery> galleryList = new ArrayList<MyGallery>();

		try {
			final String[] columns = { MediaStore.Images.Media.DATA,
					MediaStore.Images.Media._ID };
			final String orderBy = MediaStore.Images.Media._ID;

			Cursor imagecursor = managedQuery(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
					null, null, orderBy);

			if (imagecursor != null && imagecursor.getCount() > 0) {

				while (imagecursor.moveToNext()) {
					MyGallery item = new MyGallery();

					int dataColumnIndex = imagecursor
							.getColumnIndex(MediaStore.Images.Media.DATA);

					item.sdcardPath = imagecursor.getString(dataColumnIndex);

					galleryList.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// show newest photo at beginning of the list
		Collections.reverse(galleryList);
		return galleryList;
	}

}
