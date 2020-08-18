# Android10CameraBasic
Android 10 Camera basics with Scoped Storage. \
This exercise is an extension from Google documentation, Take Photo <sup>[1]</sup>

For Android 11, don't call resolveActivity
```
// Don't call resolve in Android 11, API 30
// if (takePictureIntent.resolveActivity(packageManager) != null) {
```

### Camera Basics
<img width="220" src="https://user-images.githubusercontent.com/1282659/89721363-514ab000-d9a2-11ea-9427-12926681de30.jpg"> <img width="220" src="https://user-images.githubusercontent.com/1282659/89721366-53ad0a00-d9a2-11ea-93fc-297ed243bbf1.jpg">

### Returns bitmap
```
val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
```
Returned data is bitmap in memory

<img width="550" src="https://user-images.githubusercontent.com/1282659/89722241-f23e6880-d9ac-11ea-9bbe-6fafeb15f3cb.png">

<img width="220" src="https://user-images.githubusercontent.com/1282659/89799169-b8777a00-daf2-11ea-894e-b187cbc27e14.jpg">

Then we persist into storage following Fernando<sup>[4]</sup> tutorial.

### Persist -> photoUri (scoped storage)
```
takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
```
Returned data is null; retrieve bitmap from photoUri as described in reference <sup>[2]</sup>

<img width="900" src="https://user-images.githubusercontent.com/1282659/89722240-f10d3b80-d9ac-11ea-9434-6d80aeb0f702.png">

<img width="220" src="https://user-images.githubusercontent.com/1282659/89721368-5871be00-d9a2-11ea-8bdc-2867532fe841.jpg">

# Android 11 Scoped Storage
Scope creeped into 11 since it is no longer an option. \
Permission is required before moving media into trash bin. \
Files are no longer deleted directly.

Camera -> Save -> Load -> Overwrite -> Delete \
<img width="220" alt="camera" src="https://user-images.githubusercontent.com/1282659/90450215-c6a73680-e0ae-11ea-9397-e061d3967698.png"> <img width="220" alt="camera confirm" src="https://user-images.githubusercontent.com/1282659/90450219-c870fa00-e0ae-11ea-9449-a2c961a602c3.png"> <img width="220" alt="load" src="https://user-images.githubusercontent.com/1282659/90450222-ca3abd80-e0ae-11ea-8bac-2fbb809a4496.png"> 
<img width="220" alt="write" src="https://user-images.githubusercontent.com/1282659/90522971-f8151600-e131-11ea-9cdc-ba6622351993.png"> <img width="220" alt="trash permit" src="https://user-images.githubusercontent.com/1282659/90450227-cc048100-e0ae-11ea-85a1-1f0e99663457.png">


### Android Studio 4.0
Build #AI-193.6911.18.40.6514223, built on May 20, 2020 \
Runtime version: 1.8.0_242-release-1644-b3-6222593 x86_64 \
VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o \
macOS 10.15.5 \
GC: ParNew, ConcurrentMarkSweep \
Memory: 1981M \
Cores: 8 

### Phone
Samsung Galaxy S9 \
Model # SM-G960U \
Hardware version REV1.1 \
Android version 10 \
Kernel version 4.9.186 \
Knox version 3.4.1 

# References

1. 'Take Photo' Google camera tutorial with code \
https://developer.android.com/training/camera/photobasics#java

2. "Getting null uri in onActivityResult after take image capture [duplicate]", answer by bhagyawant biradar, March 27, 2019 \
https://stackoverflow.com/questions/55370426/getting-null-uri-in-onactivityresult-after-take-image-capture

3. "How to get Bitmap from an Uri", StackOverflow \
https://www.generacodice.com/en/articolo/730776/How-to-get-Bitmap-from-an-Uri

4. "Working with Scoped Storage" by Fernando García Álvarez, October 18, 2019 \
https://proandroiddev.com/working-with-scoped-storage-8a7e7cafea3

5. "Scoped Storage on Android 11" by Fernando García Álvarez, February 19, 2020 \
https://proandroiddev.com/scoped-storage-on-android-11-2c5da70fb077

6. "Android Camera App with Examples" by Android Tutorial \
https://www.tutlane.com/tutorial/android/android-camera-app-with-examples

7. "Scoped Storage on Android 11" by Fernando García Álvarez on DroidCon, March 24, 2020 \
https://www.droidcon.com/news-detail?content-id=/repository/collaboration/Groups/spaces/droidcon_hq/Documents/public/news/android-news/Scoped%20Storage%20on%20Android%2011

8. Google documentation Scoped storage sample \
https://github.com/android/storage-samples

9. MediaStore - Uri to query all types of files (media and non-media) \
https://stackoverflow.com/questions/10384080/mediastore-uri-to-query-all-types-of-files-media-and-non-media
