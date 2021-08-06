package pjh.practice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URI;

//TODO: 졸작 위한 테스트
public class MainActivity extends AppCompatActivity {

    ImageView image;
    Button upload, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //레이아웃 바인딩
        image = findViewById(R.id.image);
        upload = findViewById(R.id.upload);
        delete = findViewById(R.id.delete);
        //Storage 인스턴스 만들기
        FirebaseStorage storage = FirebaseStorage.getInstance();
        //가져오기 업데이트를 위한 참조 만들기 (파일을 가리키는 포인터)
        StorageReference storageRef = storage.getReference();
        //업로드 버튼 클릭
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //저장소에 Uri를 이용해 파일 업로드
                Uri file = null;
                StorageReference riversRef = storageRef.child("images/image.jpg");
                if(!file.equals(null)) {
                    UploadTask uploadTask = riversRef.putFile(file);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override//업로드 실패
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), "이미지 업로드가 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override//업로드 성공
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            image.setImageURI(file);
                            Toast.makeText(getApplicationContext(), "이미지가 업로드가 성공했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        //삭제 버튼 클릭
        delete.setOnClickListener(new View.OnClickListener() {
            //저장소에서 삭제
            @Override
            public void onClick(View v) {
                StorageReference desertRef = storageRef.child("images/image.jpg");
                desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override//삭제 성공
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "이미지가 삭제가 성공했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override//삭제 실패
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "이미지가 삭제가 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}