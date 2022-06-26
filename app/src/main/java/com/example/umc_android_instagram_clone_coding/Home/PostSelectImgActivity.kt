package com.example.umc_android_instagram_clone_coding.Home

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.GridLayoutManager
import com.example.umc_android_instagram_clone_coding.Data.PostSelectImg
import com.example.umc_android_instagram_clone_coding.R
import com.example.umc_android_instagram_clone_coding.databinding.ActivityPostSelectImgBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PostSelectImgActivity: AppCompatActivity() {
    lateinit var binding: ActivityPostSelectImgBinding
    private var imgDatas = ArrayList<PostSelectImg>()
    var fbStorage : FirebaseStorage? = FirebaseStorage.getInstance()
    private var selectPosition: Int? = null
    val gallery = 0
    var imgUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostSelectImgBinding.inflate(layoutInflater)

        imgDatas.apply {
            add(PostSelectImg(R.drawable.img_shop1))
            add(PostSelectImg(R.drawable.img_shop2))
            add(PostSelectImg(R.drawable.img_shop3))
            add(PostSelectImg(R.drawable.img_shop4))
            add(PostSelectImg(R.drawable.img_shop5))
            add(PostSelectImg(R.drawable.img_shop6))
            add(PostSelectImg(R.drawable.img_shop7))
            add(PostSelectImg(R.drawable.img_shop3))
        }

        val postSelectAdapter = PostSelectImgRVAdapter(imgDatas) // 더미데이터랑 Adapter 연결
        binding.postSelectImgRv.adapter = postSelectAdapter // 리사이클러뷰에 어댑터를 연결

        postSelectAdapter.setMyItemClickListener(object : PostSelectImgRVAdapter.ItemClickListener{
            override fun getClickItem(img: PostSelectImg, position: Int) {
                // 상단 ImageView에 선택한 이미지 띄우기
                binding.postSelectImg.setImageResource(img.img!!)

                // 선택 해제된 이미지 투명도 상태 원래대로
                if (selectPosition != null)
                    binding.postSelectImgRv[selectPosition!!].alpha = 1F

                // 새로 선택한 이미지 투명도 적용
                binding.postSelectImgRv[position].alpha = 0.5F
                selectPosition = position

                Log.d("ItemClickCheck", "Activity Item Click Check")
                Log.d("ItemClickCheck", "Click Img = " + binding.postSelectImgRv.get(position))
            }
        })

        binding.postSelectImgRv.layoutManager = GridLayoutManager(this,4)

        binding.postImgCancelBtn.setOnClickListener {
            finish()
        }

        binding.postSelectNextBtn.setOnClickListener {
            val intent = Intent(this, PostWriteActivity::class.java)
            intent.putExtra("img", imgDatas[selectPosition!!].img)
            startActivity(intent)
        }

        binding.selectImgMultiBtn.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(Intent.createChooser(intent, "LOAD PICTURE"), gallery)
        }

        setContentView(binding.root)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("DataCheck", "Intent = " + intent.type)
        if (requestCode == gallery) {
            if (resultCode == RESULT_OK) {
                imgUri = data?.data
                try {
                    var bitmap : Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imgUri)
                    binding.postSelectImg.setImageBitmap(bitmap)

                    Log.d("DataCheck", "imgUri = " + imgUri.toString())

                    imageUpload()

                } catch (e:Exception) {
                    Toast.makeText(this, "$e", Toast.LENGTH_SHORT).show()
                }
            }
        } else {

        }
    }

    private fun imageUpload() {
        // var timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        // var imgFileName = "IMAGE_" + timeStamp + "_.png"
        // var storageRef: StorageReference = fbStorage?.reference?.child("images")?.child(imgFileName)
        var storageRef: StorageReference = fbStorage!!.reference.child("image")
        // var

        // Log.d("DataCheck", "Image Upload Function Check")
        // Log.d("DataCheck", storageRef.toString())

        /*
        storageRef?.putFile(imgUri!!)?.addOnSuccessListener {
            Log.d("DataCheck", "ImageUploadSuccess")
        }?.addOnFailureListener { e ->
            Log.w("DataCheck", "ImageUploadFailed", e)
        }
        */
    }
}