# React Native In-App update

With `react-native-update-in-app` library you can easily implement in-app updates in your **React Native** app using CDN or any other file server. In-app updates (like [Google Play In-app updates](https://developer.android.com/guide/playcore/in-app-updates)) allows your app users to download and install update right in your app UI.



## Example

It's best to see it with your own eyes 😉

```
git clone https://github.com/NepeinAV/react-native-update-in-app.git \
    && cd react-native-update-in-app

yarn
yarn example android
```

## Installation

⚠️ Version `1.x.x` supports React Native 0.65+ (old architecture) and TurboModules (new architecture) with React Native 0.70+.

⚠️ To make it work with previous React Native versions (0.63 - 0.64) you should install `0.4.3` version of library.

```sh
yarn add react-native-update-in-app
```

React Native should automatically link the library.

## Usage

This library provides an API to download APK file and install it, but does not provide the UI (React Native developer should implement it by himself), because every app has its own unique design and requirements.

In typical use case you should follow these steps:

### Step 1

Check that your app has an update. You can use whatever you want to send the request to the server and retrieve info. For example, you can use `fetch` to get `update.json` file and compare your app current version with server version:

```js
import React, { useCallback, useState } from 'react';

import { AppUpdate } from 'react-native-update-in-app';

const [updateData, setUpdateData] = useState<{
    url: string;
    updateMessage: string;
} | null>(null);

<...>

const checkUpdate = useCallback(async () => {
    if (Platform.OS === 'ios') {
        Alert.alert('iOS is not supported');

        return false;
    }

    const result = await fetch(
        'https://raw.githubusercontent.com/NepeinAV/react-native-update-in-app/master/example/app-updates/update.json',
    );
    const data = await result.json();
    const currentVersionCode = await AppUpdate.getVersionCode();

    if (data.versionCode <= currentVersionCode) {
        Alert.alert('Update was not found');
    } else {
        setUpdateData(data);
    }
}, []);

<...>
```

### Step 2

If you decide that your app needs update, then request library to download APK file like that:

```
AppUpdate.downloadApp(updateData.url);
```

`AppUpdate.downloadApp` method starts background service that will download your APK file. The APK continues to download even if the app in background state.

But how do you now when the file has been downloaded? The library API has event called `onDownloadProgress`, that you need to listen via `AppUpdate.onDownloadProgress()`. This event has four states: `start`, `downloading`, `end` and `error`. You can implement it like that:

```
import React, { useCallback, useState } from 'react';

import { AppUpdate } from 'react-native-update-in-app';

<...>

const [downloadProgress, setDownloadProgress] = useState(0);
const [isApkLoaded, setApkLoaded] = useState(false);
const [apkName, setApkName] = useState<string | null>(null);
const [isApkLoading, setDownloading] = useState(false);

<...>

AppUpdate.onDownloadProgress(async (event) => {
    if (event.status === 'start') {
        setDownloadProgress(0);
        setDownloading(true);
        setApkLoaded(false);
        setApkName(null);

        return;
    }

    if (event.status === 'downloading') {
        setDownloadProgress(event.progress);

        return;
    }

    if (event.status === 'end') {
        setDownloadProgress(100);
        setDownloading(false);

        setApkLoaded(true);
        setApkName(event.apkFileName);

        await AppUpdate.installApp(event.apkFileName);

        return;
    }

    if (event.status === 'error') {
        Alert.alert(event.errorMessage);

        setDownloadProgress(0);
        setDownloading(false);
        setApkLoaded(false);
        setApkName(null);

        return;
    }
});

<...>
```

### Step 3

When your APK was downloaded you can install it. Just call this:

```
AppUpdate.installApp(apkName);
```

It will call system's default package installer with your APK file.

That's it! 😎

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
