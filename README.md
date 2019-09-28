# Android Custom Lint
This example explains a couple of tools about CheckStyle, Lint, PMD, FindBugs, SpotBugs, Detekt, CPD including Custom Lint for Android.

## Structure
- **app** module : the sample example that imports lintrules module
- **lintrules** module : Custom lint project
- **buildsystem** folder
    - **dependencies.gradle** : dependency version definition
    - **config** : static analysis configs
    - **tools** : static analysis gradle tasks (checkstyle, detekt, findbugs, pmd, spotbugs)

## Custom lint implementation
- MissingBaseClass (Done)
- InvalidAnnotationImport (Done)
- BigSizedBitmapUsed (Done)
- MisplaceId (TODO)

## References
- [Write Custom Line Rules](http://tools.android.com/tips/lint-custom-rules) from Android Tool team
- [Building Custom Lint Checks in Android](https://www.bignerdranch.com/blog/building-custom-lint-checks-in-android/) by Matt Compton

## License
    Copyright 2019 Daehee Han
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0
       
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
