package pjh.practice;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

//TODO: 졸작 위한 테스트
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Storage 인스턴스 만들기
        FirebaseStorage storage = FirebaseStorage.getInstance();
        //가져오기 업데이트를 위한 참조 만들기 (파일을 가리키는 포인터)
        StorageReference storageRef = storage.getReference();
    }
}