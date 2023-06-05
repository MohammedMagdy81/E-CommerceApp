package com.example.e_commerce.mvvm

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.MyApp
import com.example.e_commerce.data.User
import com.example.e_commerce.utils.RegisterValidation
import com.example.e_commerce.utils.Resources
import com.example.e_commerce.utils.validateEmail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserAccountViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val app: Application,
    private val storage: StorageReference
) : AndroidViewModel(app) {

    private val _user = MutableStateFlow<Resources<User>>(Resources.Ideal())
    val user = _user.asStateFlow()

    private val _updateInfo = MutableStateFlow<Resources<User>>(Resources.Ideal())
    val updateInfo = _updateInfo.asStateFlow()

    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        viewModelScope.launch { _user.emit(Resources.Loading()) }
        firestore.collection("users").document(auth.uid!!).get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                user?.let {
                    viewModelScope.launch { _user.emit(Resources.Success(it)) }
                }
            }.addOnFailureListener {
                viewModelScope.launch { _user.emit(Resources.Error(it.message.toString())) }
            }
    }

    fun updateUser(user: User, imageUri: Uri?) {
        val areInputsValid = validateEmail(user.email) is RegisterValidation.Success
                && user.firstName.trim().isNotEmpty()
                && user.firstName.trim().isNotEmpty()
        if (!areInputsValid) {
            viewModelScope.launch { _updateInfo.emit(Resources.Error("تاكد من استكمال جميع البيانات!")) }
            return
        }
        viewModelScope.launch { _updateInfo.emit(Resources.Loading()) }

        if (imageUri == null) {
            saveUserInfo(user, true)
        } else {
            saveUserInfoWithNewImage(user, imageUri)
        }


    }

    private fun saveUserInfoWithNewImage(user: User, imageUri: Uri) {
        viewModelScope.launch {
            try {
                val imageBitmap = MediaStore.Images.Media.getBitmap(
                    getApplication<MyApp>().contentResolver,
                    imageUri
                )
                val byteArrayOutputStream = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val imageByteArray = byteArrayOutputStream.toByteArray()
                val imageDirectory = storage.child("profileImages/${auth.uid}/${UUID.randomUUID()}")
                val result = imageDirectory.putBytes(imageByteArray).await()
                val uri = result.storage.downloadUrl.await().toString()
                saveUserInfo(user.copy(imagePath = uri), false)
            } catch (e: Exception) {
                viewModelScope.launch { _updateInfo.emit(Resources.Error(e.message.toString())) }
            }
        }


    }

    private fun saveUserInfo(user: User, shouldRetrieveOldImage: Boolean) {
        firestore.runTransaction { transiction ->
            val documentRef = firestore.collection("users")
                .document(auth.uid!!) // this is document belong to current user
            if (shouldRetrieveOldImage) {
                val currentUser = transiction.get(documentRef).toObject(User::class.java)
                val newUser = user.copy(imagePath = currentUser?.imagePath ?: "")
                transiction.set(documentRef, newUser)
            } else {
                transiction.set(documentRef, user)
            }

        }.addOnSuccessListener {
            viewModelScope.launch { _updateInfo.emit(Resources.Success(user)) }
        }.addOnFailureListener {
            viewModelScope.launch { _updateInfo.emit(Resources.Error(it.message.toString())) }
        }

    }


}








