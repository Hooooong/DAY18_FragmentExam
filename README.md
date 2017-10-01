# Fragment 를 이용한 화면 분할 예제

### 설명
____________________________________________________

![Fragment 화면 분할](https://github.com/Hooooong/DAY18_FragmentExam/blob/master/image/FragmentExam.gif)

- Fragment 를 이용한 화면 분할 예제

### KeyPoint
____________________________________________________

- Fragment

  - 참조 : [Fragment](https://github.com/Hooooong/DAY18_Fragment)

- 가로, 세로 화면 Layout

  ![layout 폴더 구조](https://github.com/Hooooong/DAY18_FragmentExam/blob/master/image/layout%20%ED%8F%B4%EB%8D%94%20%EA%B5%AC%EC%A1%B0.PNG)

  - 가로, 세로 화면 layout 을 따로 작성해줘야 한다.

  - Layout 폴더를 `layout-land` 로 생성을 하면 가로 화면일 때 layout을 호출해준다.(동일한 이름)

  - 가로, 세로 화면 구별 코드

  ```java
  if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
      // 새로모드일 때
  }else{
      // 가로모드일 때
  }
  ```

- Click 이벤트 처리

  - `CallBack` Interface 를 만들어 호출한다.

  - `onAttach()` 메소드를 호출 할 때 Context를 받기 때문에 `onAttach()` 에서 Casting 해준다.

  - ListFragment.java

  ```java
  public class ListFragment extends Fragment {

      private CallBack callBack;

      // Adapter 에 CallBack 객체를 넘겨줘 처리

      // 생략
      @Override
      public void onAttach(Context context) {
          super.onAttach(context);
          this.context = context;

          if(context instanceof CallBack){
              this.callBack = (CallBack) context;
          }
      }

      public interface CallBack{
          void goDetail(String value);
      }
  }
  ```

  - MainActivity.java

  ```java
  public class MainActivity extends AppCompatActivity implements ListFragment.CallBack {
    // 생략

    @Override
    public void goDetail(String value) {
      // 이벤트 처리 작성 구역
    }
  }
  ```

  - CustomAdapter.java

  ```java
  itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          callBack.goDetail(textView.getText().toString());
      }
  });
  ```

- Fragment 에 데이터 넘겨주기

  - `Bundle` 객체를 통해 넘겨준다.

  ```java
  DetailFragment detailFragment = new DetailFragment();
  Bundle data = new Bundle();
  data.putString("value", value);
  detailFragment.setArguments(data);
  ```

  - 데이터를 넘겨줄 필요 없이 Fragment 를 변경하는 경우

  ```java
  // getSupportFragmentManager().findFragmentById() 를 통해 Fragment를 찾을 수 있다.
  DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragment2);
  detailFragment.setTextView(value);
  ```

### Code Review
____________________________________________________

- MainActivity.java

  - 가로, 세로 화면일 때 로직 처리 구역

  - 가로 화면일 때는 Fragment 의 view 만 수정하면 되고, 세로 화면일 때는 Fragment 를 add 해야 한다.

  - Click 이벤트 `CallBack` 처리를 해준다.

  ```java
  public class MainActivity extends AppCompatActivity implements ListFragment.CallBack {

      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          // 기준이 되는 Activity 는 activity_main.xml 을 사용
          // 가로모드 에서 되는 Activity 는 activity_main.xml(layout-land) 를 사용
          setContentView(R.layout.activity_main);

          if(savedInstanceState != null)
              return;

          init();
      }

      private void init(){
          if(getResources().getConfiguration().orientation
                  == Configuration.ORIENTATION_PORTRAIT){
              // 새로모드일 때
              // 프래그먼트를 Setting
              initFragment();
          }else{
              // 가로모드일 때
              // 작업 없음
          }
      }

      /**
       * Fragment 초기화
       */
      private void initFragment(){
          // Fragment 부착하기
          getSupportFragmentManager()
                  .beginTransaction()
                  .add(R.id.container, new ListFragment())
                  .commit();
      }

      /**
       * Fragment 더하기
       */
      private void addFragment(String value){
          //Fragment 에 값을 넘겨주는 방법
          DetailFragment detailFragment = new DetailFragment();
          Bundle data = new Bundle();
          data.putString("value", value);
          detailFragment.setArguments(data);

          getSupportFragmentManager()
                  .beginTransaction()
                  .add(R.id.container, detailFragment)
                  .addToBackStack(null)
                  .commit();
      }

      @Override
      public void goDetail(String value) {
          if(getResources().getConfiguration().orientation
                  == Configuration.ORIENTATION_PORTRAIT){
              // 세로모드일 때
              // DetailFragment 를 Setting
              addFragment(value);
          }else{
              // 가로모드일 때
              // 현재 레이아웃에 삽입되어 있는 Fragment 를 가져온다.
              DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragment2);
              detailFragment.setTextView(value);
          }
      }
  }
  ```

- ListFragment.java

  - List를 보여주는 Fragment

  ```java
  public class ListFragment extends Fragment {

      private Context context;
      private RecyclerView recyclerView;
      private CustomAdapter adapter;
      private CallBack callBack;

      public ListFragment() {
          // Required empty public constructor
      }

      @Override
      public void onAttach(Context context) {
          super.onAttach(context);
          this.context = context;

          if(context instanceof CallBack){
              this.callBack = (CallBack) context;
          }
      }

      @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState) {
          View view = inflater.inflate(R.layout.fragment_list, container, false);
          init(view);
          return view;
      }

      private void init(View view){
          recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
          adapter = new CustomAdapter(context, callBack,load() );
          recyclerView.setAdapter(adapter);
          recyclerView.setLayoutManager(new LinearLayoutManager(context));
      }

      private List<String> load(){
          List<String> data = new ArrayList<>();

          for(int i = 0; i<100; i++){
              data.add(i+"");
          }
          return data;
      }

      public interface CallBack{
          void goDetail(String value);
      }
  }
  ```

- DetailFragment.java

  - List를 Click 했을 경우 상세 화면을 보여주는 Fragment

  - `Bundle` 처리나 TextView 변경 처리만 하면 된다.

  ```java
  public class DetailFragment extends Fragment {

      private TextView textView;

      public DetailFragment() {
          // Required empty public constructor
      }

      @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState) {
          // Inflate the layout for this fragment
          View view = inflater.inflate(R.layout.fragment_detail, container, false);
          init(view);
          return view;
      }

      private void init(View view) {
          textView = (TextView)view.findViewById(R.id.textView);
          // 값 꺼내기
          Bundle bundle = getArguments();
          if(bundle != null){
              setTextView(bundle.getString("value"));
          }
      }

      public void setTextView(String text){
          textView.setText(text);
      }
  }
  ```

- CustomAdatper.java

  - ListFragment.java 에 필요한 CustomAdatper 클래스이다.

  - Click 이벤트에 대한 `CallBack` 호출만 하면 된다.

  ```java
  public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Holder> {

      List<String> data;
      Context context;
      ListFragment.CallBack callBack;

      public CustomAdapter(Context context, ListFragment.CallBack callBack, List<String> data) {
          this.context = context;
          this.callBack = callBack;
          this.data = data;
      }

      @Override
      public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
          View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
          return new Holder(view);
      }

      @Override
      public void onBindViewHolder(Holder holder, int position) {

          holder.setTextView(data.get(position));
      }

      @Override
      public int getItemCount() {
          return data.size();
      }

      public class Holder extends RecyclerView.ViewHolder{

          private TextView textView;

          public Holder(View itemView) {
              super(itemView);
              textView = (TextView)itemView.findViewById(R.id.textView);
              itemView.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      callBack.goDetail(textView.getText().toString());
                  }
              });
          }

          public void setTextView(String text) {
              textView.setText(text);
          }
      }
  }
  ```

- activity_main.xml(가로)

  ```xml
  <?xml version="1.0" encoding="utf-8"?>
  <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto"
      xmlns:tools="http://schemas.android.com/tools"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      android:orientation="horizontal"
      tools:context="com.hooooong.fragmentexam.MainActivity">

      <!-- Fragment 를 xml에서 호출한다. -->
      <!-- name 에 Fragment의 .java 파일 경로를 작성한다. -->
      <fragment
          android:id="@+id/fragment"
          android:name="com.hooooong.fragmentexam.ListFragment"
          android:layout_width="0dp"
          android:layout_height="match_parent"
          android:layout_weight="1" />

      <fragment
          android:id="@+id/fragment2"
          android:name="com.hooooong.fragmentexam.DetailFragment"
          android:layout_width="0dp"
          android:layout_height="match_parent"
          android:layout_weight="2" />
  </LinearLayout>

  ```

- activity_main.xml(세로)

  ```xml
  <?xml version="1.0" encoding="utf-8"?>
  <android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto"
      xmlns:tools="http://schemas.android.com/tools"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      tools:context="com.hooooong.fragmentexam.MainActivity">

      <FrameLayout
          android:id="@+id/container"
          android:layout_width="0dp"
          android:layout_height="0dp"
          app:layout_constraintTop_toTopOf="parent"
          app:layout_constraintLeft_toLeftOf="parent"
          app:layout_constraintRight_toRightOf="parent"
          app:layout_constraintBottom_toBottomOf="parent">
      </FrameLayout>
  </android.support.constraint.ConstraintLayout>

  ```

- fragment_list.xml

  ```xml
  <FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:tools="http://schemas.android.com/tools"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      tools:context="com.hooooong.fragmentexam.ListFragment">
      <android.support.v7.widget.RecyclerView
          android:id="@+id/recyclerView"
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          tools:listitem="@layout/item_list" />
  </FrameLayout>
  ```

- fragment_detail.xml

  ```xml
  <FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:tools="http://schemas.android.com/tools"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      android:background="#FFFFFF"
      android:clickable="true"
      tools:context="com.hooooong.fragmentexam.DetailFragment">

      <!-- TODO: Update blank fragment layout -->
      <TextView
          android:id="@+id/textView"
          android:layout_width="wrap_content"
          android:layout_height="wrap_content"
          android:text="DETAIL"
          android:textSize="20dp"
          android:textStyle="bold"
          android:layout_gravity="center"/>

  </FrameLayout>
  ```
