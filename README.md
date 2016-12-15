WeLegends
=========

# Config for Developers
You will need a Riot [Api Key](https://developer.riotgames.com/sign-in) for testing the app.
Config the key in your gradle `local.properties` before start programming. 
```
ApiKey=<YOUR_API_KEY>
```

You can also change the [Retrofit](https://github.com/square/retrofit) log level adding this property to your `local.properties`
	
	- Allowed levels are: NONE, BASIC, HEADERS, BODY(default)
```
HttpLoggingInterceptorLevel=<LEVEL>
```

# Pull Requests
I welcome and encourage all pull requests. Here are some basic rules to follow to ensure timely addition of your request:
  1. Match coding style (braces, spacing, etc.) This is best achieved using CMD+Option+L (on Mac) or Ctrl+Alt+L on Windows to reformat code with Android Studio defaults.
  2. If its a feature, bugfix, or anything please only change code to what you specify.
  3. Please keep PR titles easy to read and descriptive of changes, this will make them easier to merge.
  4. Pull requests _must_ be made against `develop` branch. Any other branch (unless specified by the maintainers) will get rejected.
  5. Have fun!

# Maintained By
[IagoCanalejas](https://github.com/iagocanalejas) ([@iagocanalejas](https://twitter.com/Iagocanalejas))
[Andy](https://github.com/andy135) ([@ANDYear21](https://twitter.com/ANDYear21))
