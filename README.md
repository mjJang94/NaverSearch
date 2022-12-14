
## 네이버 검색 API를 활용한 Android 검색 앱

평소에 자주 사용하는 네이버 앱 기능 중 일부 검색 기능을 구현해보았습니다.

구현된 코드는 계속해서 보완하며 적용하고 싶은 것들을 추가할 예정입니다.

### 이미지 검색 API 가이드  
https://developers.naver.com/products/service-api/search/search.md

### 개발 환경   

<a href="https://https://developer.android.com/studio/intro?hl=ko"><img src="https://img.shields.io/badge/Android Studio-3DDC84?style=flat-square&logo=Android Studio&logoColor=white"/></a>
<a href="https://kotlinlang.org/docs/releases.html#release-details"><img src="https://img.shields.io/badge/Kotlin 1.6.21-7F52FF?style=flat-square&logo=Kotlin&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Git-F05032?style=flat-square&logo=Git&logoColor=white"/>


### Architecture
MVVM + 구글 권장 아키텍처

<img src="/architecture.png" width="600px" height="350px"></img><br/>

### 구성 Library
- Paging3
- LifeCycle
- DataBinding
- Retrofit2
- Glide
- Dagger-hilt
- Coroutines
- Timber

----

## Project Review

1. 구글에서 권장하는 안드로이드 아키텍처를 기반으로 관심사 분리를 위해 구조를 **UI Layer, Domain Layer, Data Layer**로 분리하였습니다.

- **UI Layer** - 유저 상호작용, 외부 입력 값(네트워크, DB)에 의해 데이터가 변할때마다 변경사항에 대한 UI 업데이트를 위해 데이터 홀더 클래스인 ViewModel을 사용하였습니다. 각 Activity, Fragment에서 필요로 하는 ViewModel이 관리하는 데이터 혹은 이벤트를 Observe하여 변화에 따라 UI 요소들을 업데이트 하도록 구성하였습니다. 또한 Lifecycle에 따라 불필요한 observe를 하지 않도록 고민하였습니다.

- **Domain Layer** - 복잡한 비즈니스 로직, 혹은 여러 ViewModel에서 재사용될 수 있는 로직을 담당하기 위해 추가하였습니다. 초기 버전인 1.0.0에서는 효율 보다는 비용이 더 많이 들었지만 추가 개발에 의해 ViewModel이 늘어나거나 동일한 로직이 필요할때 유용하게 사용할 수 있을 것 같아 추가하게 되었습니다. 해당 레이어에서는 각각 하나의 기능을 구현한 UseCase 클래스로 구성되어 있고, ViewModel에서 필요한 기능에 따라 알맞는 UseCase를 주입받아 사용하도록 구현했습니다.

- **Data Layer** - 앱의 데이터 생성, 저장, 삭제를 담당하고, 이제 따라 local, remote 데이터를 처리하기 위한 데이터 소스 클래스로 구성되어 있습니다. 하나의 데이터 소스로 여러가지 데이터 처리를 할 수 있도록 구현하였습니다.

2. 원칙적으로 ViewModel은 Presentation layer에 위치하고 있으므로 안드로이드 플랫폼과 최대한 관계가 없도록 구현하려고 시도했습니다.    
그러기 위해서 LiveData로 처리하던 모든 이벤트를 Sealed class(Event) + SharedFlow 조합으로 변경하여 Event 타입에 따라 Activity/Fragment에서 처리하도록 분기하였습니다.    
또한 홈 버튼을 눌러서 화면이 Background에 진입하게 된 경우, 변경된 UIEvent 데이터에 의해 화면을 업데이트 해줄 필요가 없으므로 UI가 사용자에게 보여지고 있지 않을 땐 데이터를 observe 하지 않도록 Lifecycle.repeatOnLifecycle()을 사용하였습니다.    
해당 메소드를 사용하게 된 이유는 지정한 Lifecycle 상태에 맞춰 알아서 코루틴 블록을 실행할지 취소할지 스스로 판단하기 때문에, 부가적인 코드를 줄일 수 있었습니다.

3. 리사이클러뷰 무한 스크롤 기능을 향상하기 위해 몇 가지 설정을 추가했습니다.

- **Glide.skipMemoryCache(false)** - 메모리가 극한으로 한정적인 상태인 것과 같이 특이한 케이스가 아닐 땐 캐싱을 위해 false로 지정하여 캐싱를 통해 빠른 이미지 호출 및 불필요한 네트워크 호출을 줄입니다.

- **Glide.format(DecodeFormat.PREFER_RGB_565)** - 썸네일 수준의 이미지 이므로 기본 포맷인 ARGB_8888을 RGB_565로 변경하여 색상 품질을 낮춰 메모리 효율을 올립니다. (약 50% 상승 기대)

- **Glide ImageView 고정 크기** - Glide를 통해 Image를 적용하려는 ImageView의 크기가 고정이 아닐 때 사용중인 기기 기준으로 이미지 해상도가 결정되어 의도치 않은 메모리 소비로 이어지고, GC의 호출이 증가함에 따라 성능 저하를 일으킬 수 있습니다. 이를 방지하기 위해 고정크기를 사용하였습니다.

### 테스트 코드

**추가 예정**

### 동작 화면




