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
<img width="220" src="https://user-images.githubusercontent.com/1282659/89721367-57409100-d9a2-11ea-86d4-a5f0ca4431f8.jpg">

### Persist -> photoUri (scoped storage)
```
takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
```
<img width="220" src="https://user-images.githubusercontent.com/1282659/89721368-5871be00-d9a2-11ea-8bdc-2867532fe841.jpg">

# References

1. 'Take Photo' Google camera tutorial with code \
https://developer.android.com/training/camera/photobasics#java

2. "Getting null uri in onActivityResult after take image capture [duplicate]", answer by bhagyawant biradar, March 27, 2019 \
https://stackoverflow.com/questions/55370426/getting-null-uri-in-onactivityresult-after-take-image-capture

3. "How to get Bitmap from an Uri", StackOverflow \
https://www.generacodice.com/en/articolo/730776/How-to-get-Bitmap-from-an-Uri
