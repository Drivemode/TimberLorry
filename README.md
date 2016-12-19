# TimberLorry

[![Join the chat at https://gitter.im/Drivemode/TimberLorry](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/Drivemode/TimberLorry?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[![Gitter](http://img.shields.io/badge/Gitter-Join%20Chat-brightgreen.svg?style=flat)](https://gitter.im/Drivemode/TimberLorry?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-TimberLorry-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1692)
[![License](http://img.shields.io/badge/License-Apache%202-brightgreen.svg?style=flat)](https://github.com/Drivemode/TimberLorry/blob/master/LICENSE)
[![Circle CI](https://circleci.com/gh/Drivemode/TimberLorry/tree/master.svg?style=shield)](https://circleci.com/gh/Drivemode/TimberLorry/tree/master)

Periodical log collector framework.

![Timber Lorry](https://farm4.staticflickr.com/3746/11950116365_df10a41139_z_d.jpg)  
Photo Licensed under [CC BY-NC 2.0](https://creativecommons.org/licenses/by-nc/2.0/) by jbdodane

## Usage

In your `Application`, initialize `TimberLorry` with configuration on your preference.
After this, you can schedule periodical log collection.

```java
public class TimberLorryApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TimberLorry.initialize(new Config.Builder()
                .collectIn(10) // collect log data every 10 seconds.
                .serializeWith(new GsonSerializer())
                .addOutlet(new LogcatOutlet()).build(this));
        TimberLorry.getInstance().schedule();
    }
}
```

## How TimberLorry works

TimberLorry is a log collector without data loss for some reasons(e.g. reboot, exit application).
TimberLorry will store logs in the buffer(internal database by default), and periodically send it to an endpoint.
If no internet connection available, the periodical work will not executed.

## Building Blocks

TimberLorry has some gateways to customize payloads, serialization logic, and buffer storage, and log endpoint.
All customization will be injected with `Config` object.

### Custom Payload

Payload is an entity object containing log data.
The following snippet shows an example code.

```java
public class ButtonClick implements Payload {
    private final long timeInMillis;

    public ButtonClick(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }
}
```

Payloads will be serialized by `Serializer` in the buffer.

### Custom Serializer

You can add your own serializer.

Here is an example using Gson.

```java
public class GsonSerializer implements Serializer {
    @Override
    public String serialize(Payload payload) {
        return new Gson().toJson(payload);
    }
}
```

We have this `GsonSerializer` in `plug` project.

### Custom BufferResolver

If you would like to use another log data storage(e.g. internal storage, shared preferences), you can
implement your own `BufferResolver`.

```java
public class SharedPrefBufferResolver extends AbstractBufferResolver {
    private final Application application;

    public SharedPrefBufferResolver(Application application, @NonNull AccountManager accountManager, @NonNull ContentResolver resolver, Account account) {
        super(accountManager, resolver, account);
        this.application = application;
    }

    @Override
    public void save(Serializer serializer, Payload payload) {
        Record record = new Record(payload.getClass(), serializer.serialize(payload));
        // ... write record on the preference.
    }

    @Override
    public void save(Serializer serializer, Payload... payloads) {
        for (Payload payload : payloads) {
            Record record = new Record(payload.getClass(), serializer.serialize(payload));
            SharedPreferences pref = SharedPreferences.getDefaultSharedPreferences(application);
            // ... write record on the preference.
        }
    }

    @Override
    public List<Record> fetch() {
        List<Record> records = new ArrayList<>();
        SharedPreferences pref = SharedPreferences.getDefaultSharedPreferences(application);
        // ... find records on the preference and convert them to Record.
        return records;
    }

    @Override
    public void remove(Record record) {
        SharedPreferences pref = SharedPreferences.getDefaultSharedPreferences(application);
        // ... find record on the preference and remove it.
    }

    @Override
    public void clear() {
        SharedPreferences pref = SharedPreferences.getDefaultSharedPreferences(application);
        pref.edit().clear().apply();
    }
}
```

### Custom Log Endpoint

You can add log endpoint with custom `Outlet`.
If you want to filter `Payload` in your `Outlet`, check the `Payload` class information in `Outlet#accept(Class<?>)` and return `true` if you accept the `Payload` in this `Outlet`.

```java
public class LogcatOutlet implements Outlet {
    public static final String TAG = LogcatOutlet.class.getSimpleName();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean accept(Class<?> payload) {
        return true; // every payload is logged with this outlet
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Result dispatch(String payload) {
        // do thread blocking work here :)
        // if something went wrong here, catch the exception here and envelope it into Result object to return.
        Utils.logV(payload);
        return Result.success();
    }

    @Override
    public String name() {
        return "LogCat";
    }
}
```

## Plugins

We have some customizations in "plug" project as plugin by default.
Currently in "plug" project, following plugins are provided.

- [Gson](https://code.google.com/p/google-gson/) serialization
- [Realm](https://github.com/realm/realm-java) as a log storage.

## Download

Via Gradle

```groovy
dependencies {
    compile 'com.drivemode:TimberLorry:1.0.0@aar'
    compile 'com.drivemode:TimberLorry-Plug:1.0.0@aar' // this is an optional
}
```

## License

```
Copyright (C) 2015 Drivemode, Inc. All rights reserved.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use
this file except in compliance with the License. You may obtain a copy of the
License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed
under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
CONDITIONS OF ANY KIND, either express or implied. See the License for the
specific language governing permissions and limitations under the License.
```
