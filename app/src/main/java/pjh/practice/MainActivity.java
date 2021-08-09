package pjh.practice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
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
    Uri file;
    FirebaseStorage storage;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //레이아웃 바인딩
        image = findViewById(R.id.image);
        upload = findViewById(R.id.upload);
        delete = findViewById(R.id.delete);
        //Storage 인스턴스 만들기
        storage = FirebaseStorage.getInstance();
        //가져오기 업데이트를 위한 참조 만들기 (파일을 가리키는 포인터)
        storageRef = storage.getReference();
        updateFile();
        //업로드 버튼 클릭
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 200);
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
                        updateFile();
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

    //Storage로 업로드 실시
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            file = data.getData();
            //저장소에 Uri를 이용해 파일 업로드
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
                        updateFile();
                        Toast.makeText(getApplicationContext(), "이미지가 업로드가 성공했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
    
    //File 업로드, 삭제 확인해 업데이트
    protected void updateFile() {
        //Glide를 이용해 이미지 업로드 되면 클라우드 저장소에서 다운로드 받아와 갱신
        //load: 불러올 파일. signature: 실시간 갱신 가능하게. into: 어떤 뷰에다가 적용?
        Glide.with(getApplicationContext())
                .load(storageRef.child("images/image.jpg"))
                .signature(new ObjectKey(System.currentTimeMillis()))
                .into(image);
    }
}