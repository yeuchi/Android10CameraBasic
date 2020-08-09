# Android10CameraBasic
Android 10 Camera basics with Scoped Storage. \
This exercise is an extension from Google documentation, Take Photo <sup>[1]</sup>



### Camera Basics
<img width="220" src="https://user-images.githubusercontent.com/1282659/89721363-514ab000-d9a2-11ea-9427-12926681de30.jpg"> <img width="220" src="https://user-images.githubusercontent.com/1282659/89721366-53ad0a00-d9a2-11ea-93fc-297ed243bbf1.jpg">

### Returns bitmap
```
val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
```
Returned data is bitmap in memory

<img width="550" src="https://user-images.githubusercontent.com/1282659/89722241-f23e6880-d9ac-11ea-9bbe-6fafeb15f3cb.png">

<img width="220" src="https://user-images.githubusercontent.com/1282659/89721367-57409100-d9a2-11ea-86d4-a5f0ca4431f8.jpg">

### Persist -> photoUri (scoped storage)
```
takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
```
Returned data is null; retrieve bitmap from photoUri as described in reference <sup>[2]</sup>

<img width="900" src="https://user-images.githubusercontent.com/1282659/89722240-f10d3b80-d9ac-11ea-9434-6d80aeb0f702.png">

<img width="220" src="https://user-images.githubusercontent.com/1282659/89721368-5871be00-d9a2-11ea-8bdc-2867532fe841.jpg">

### Android Studio 4.0
Build #AI-193.6911.18.40.6514223, built on May 20, 2020
Runtime version: 1.8.0_242-release-1644-b3-6222593 x86_64
VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
macOS 10.15.5
GC: ParNew, ConcurrentMarkSweep
Memory: 1981M
Cores: 8
Registry: ide.new.welcome.screen.force=true, dart.projects.without.pubspec=true
Non-Bundled Plugins: google-sceneform-tools

### Phone
Samsung Galaxy S9 
Model # SM-G960U
Hardware version REV1.1
Android version 10
Kernel version 4.9.186
Knox version 3.4.1

# References

1. 'Take Photo' Google camera tutorial with code \
https://developer.android.com/training/camera/photobasics#java

2. "Getting null uri in onActivityResult after take image capture [duplicate]", answer by bhagyawant biradar, March 27, 2019 \
https://stackoverflow.com/questions/55370426/getting-null-uri-in-onactivityresult-after-take-image-capture

3. "How to get Bitmap from an Uri", StackOverflow \
https://www.generacodice.com/en/articolo/730776/How-to-get-Bitmap-from-an-Uri
