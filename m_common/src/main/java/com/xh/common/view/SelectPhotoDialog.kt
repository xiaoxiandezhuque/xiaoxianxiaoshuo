package com.xh.common.view


import android.content.Context
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.xh.common.R


class SelectPhotoDialog(private val mContext: Context, private val isOpenAlbum: Boolean = true) {

    companion object {
        val TO_PHOTO_GALLERY = 1011
        val TO_CAMERA = 1021
    }

    private var btnPhotoGallery: Button? = null
    private var btnCamera: Button? = null
    private var btnCancel: Button? = null

    private var mDialog: AlertDialog? = null

    var imagePath: String? = null

    fun show() {
        if (isOpenAlbum) {
            if (mDialog == null) {
                mDialog = AlertDialog.Builder(mContext).create()
                mDialog!!.show()
                val window = mDialog!!.window ?: return
                window.setBackgroundDrawableResource(R.drawable.shape_transparent)
                val view = window.layoutInflater.inflate(R.layout.dialog_select_photo, null)
                window.setContentView(view)
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
                window.setGravity(Gravity.BOTTOM)
                btnPhotoGallery = view.findViewById(R.id.btn_photo_gallery)
                btnCamera = view.findViewById(R.id.btn_camera)
                btnCancel = view.findViewById(R.id.btn_cancel)

                btnPhotoGallery?.setOnClickListener {
                    toAlbum()
                }
                btnCancel?.setOnClickListener { dismiss() }
                btnCamera?.setOnClickListener { toCamera() }
            } else if (!mDialog!!.isShowing) {
                mDialog!!.show()
            }
        } else {

            toCamera()
        }

    }

    fun dismiss() {
        if (mDialog?.isShowing ?: false) {
            mDialog?.dismiss()
        }
    }

    fun toAlbum() {
//        PermissionModel.request(mContext as Activity, {
//            dismiss()
//            if (it) {
//                val intent = Intent()
//                intent.setType("image/*")
//                intent.setAction(Intent.ACTION_GET_CONTENT)
//                mContext.startActivityForResult(intent, TO_PHOTO_GALLERY);
//            } else {
//                "权限不足，请打开相应的权限".showToast()
//            }
//        }, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
    }

    fun toCamera() {
//        PermissionModel.request(mContext as Activity, {
//            dismiss()
//            if (it) {
//                val intent = Intent()
//                var savePath = ""
//                var storageState = Environment.getExternalStorageState()
//                if (mContext.getExternalCacheDir() == null) {
//                    return@request;
//                }
//                if (storageState.equals(Environment.MEDIA_MOUNTED)) {
//                    savePath = mContext.getExternalCacheDir().getAbsolutePath() + "/" + BaseConfig.CACHE_PATH;
//                    val savedir = File(savePath)
//                    if (!savedir.exists()) {
//                        savedir.mkdirs();
//                    }
//                }
//                if (savePath.isEmpty()) {
//                    "无法保存照片，请检查SD卡是否挂载".showToast()
//                    return@request;
//                }
//                val timeStamp = System.currentTimeMillis().toString()
//                val fileName = timeStamp + ".jpg";// 照片命名
//                val out = File(savePath, fileName);
//                imagePath = out.getPath();
//                val uri: Uri
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    uri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", out);
//
//                } else {
//                    uri = Uri.fromFile(out);
//                }
//                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.addCategory(Intent.CATEGORY_DEFAULT);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//                mContext.startActivityForResult(intent, TO_CAMERA);
//
//            } else {
//                "权限不足，请打开相应的权限".showToast()
//            }
//
//        }, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
    }

    /**
     * 转换 content:// uri

     * @param imageFile
     * *
     * @return
     */
    //    public Uri getImageContentUri(File imageFile) {
    //        String filePath = imageFile.getAbsolutePath();
    //        Cursor cursor = mContext.getContentResolver().query(
    //                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
    //                new String[]{MediaStore.Images.Media._ID},
    //                MediaStore.Images.Media.DATA + "=? ",
    //                new String[]{filePath}, null);
    //
    //        if (cursor != null && cursor.moveToFirst()) {
    //            int id = cursor.getInt(cursor
    //                    .getColumnIndex(MediaStore.MediaColumns._ID));
    //            Uri baseUri = Uri.parse("content://media/external/images/media");
    //            cursor.close();
    //            return Uri.withAppendedPath(baseUri, "" + id);
    //        } else {
    //            if (imageFile.exists()) {
    //                ContentValues values = new ContentValues();
    //                values.put(MediaStore.Images.Media.DATA, filePath);
    //
    //                return mContext.getContentResolver().insert(
    //                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    //            } else {
    //                return null;
    //            }
    //        }
    //    }

    //    private void crop(String imagePath) {
    //        File file = new File("xxx.jpg");
    //        cropImagePath = file.getAbsolutePath();
    //
    //        Intent intent = new Intent("com.android.camera.action.CROP");
    //        intent.setDataAndType(getImageContentUri(new File(imagePath)), "image/*");
    //        intent.putExtra("crop", "true");
    //        intent.putExtra("aspectX", config.aspectX);
    //        intent.putExtra("aspectY", config.aspectY);
    //        intent.putExtra("outputX", config.outputX);
    //        intent.putExtra("outputY", config.outputY);
    //        intent.putExtra("scale", true);
    //        intent.putExtra("return-data", false);
    //        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
    //        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
    //        intent.putExtra("noFaceDetection", true);
    //        startActivityForResult(intent, IMAGE_CROP_CODE);
    //    }
}
