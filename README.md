Android Media Picker
====================

# Screenshots
![Main screen](/screenshots/activity_folder.jpg) ![Menu closed](/screenshots/activity_subfolder.jpg)


## Why?
 * Other libraries don't offer multi-pick options
 * Other libraries don't offer camera select option
 * They dont offer UI customisability
 * They are not build for latest Release of Android
 
## Features
 * Multi-Pick option
 * Select from camera
 * ActionBar color customisability

# Requirements
The library requires Android **API Level 14+**.


# Download
## In the build.gradle (at the module level) add the new dependency:
```
    compile 'com.prashantmaurice.android:media-picker:0.0.0.2'
```

Getting Started
===============

Add necessary permissions in your AndroidManifest.xml
```xml

<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

Start the PolyPicker activity and get the result back.

```java
private void getImages() {
    Intent intent = new MediaPicker.IntentBuilder()
            .selectMultiple(true)
            .build(MainActivity.this);
    startActivityForResult(intent, REQUEST_PICK_MULTIPLE);
}


@Override
protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
	super.onActivityResult(requestCode, resultCode, intent);

	if(resultCode==RESULT_OK){
        switch (requestCode){
            case REQUEST_PICK_MULTIPLE:
                MediaPicker.ResultParser.ResultData dataObject = MediaPicker.ResultParser.parseResult(data);
                List<ImageObj> pics = dataObject.getSelectedPics();
                if(pics.size()>0){
                    Uri uri = pics.get(0).getURI();
                    Utils.showToast(this,"Picked "+uri.getPath());
                }
                Utils.showToast(this,"Picked "+pics.size()+" images");
                break;
        }
    }else{
        Utils.showToast(this,"Cancelled Request");
    }
}


```
 
Request large heap memory using "largeHeap" attribute for your application. This will avoid application to
crash on low memory devices. The side effect would be that your application may force
other applications to be kicked out of memory. Nothing very severe.

```xml

<application
		android:icon="@drawable/ic_launcher"
		android:label="@string/app_name"
		android:largeHeap="true">
		.
		.
</application>

```
 
## Features in pipeline
 * Custom Folder Options (eg : select from Facebook)
 * Select Videos
 * Select Files
 
Contributing
==============
 
 Please fork this repository and contribute back using [pull requests](https://github.com/prashantmaurice/AndroidMediaPicker/pulls).
 Please follow Android code [style guide](https://source.android.com/source/code-style.html)

 
Developed by
============
 
  * Prashant Maurice - <sabertoothmaurice@gmail.com> [Linkedin](https://in.linkedin.com/in/prashantmaurice) [Twitter](https://twitter.com/MauricePrashant)
  * Dipak Bansal


# License

```
Copyright 2016 Prashant maurice

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

```