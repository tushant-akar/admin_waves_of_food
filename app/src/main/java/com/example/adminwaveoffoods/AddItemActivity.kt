package com.example.adminwaveoffoods

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.adminwaveoffoods.databinding.ActivityAddItemBinding
import com.example.adminwaveoffoods.model.AllMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddItemActivity : AppCompatActivity() {

    //Food Item Details
    private lateinit var foodName: String
    private lateinit var foodPrice: String
    private lateinit var foodDescription: String
    private lateinit var foodIngredient: String
    private var foodImageUri: Uri? = null

    //Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private val binding: ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Initialize Firebase
        auth = FirebaseAuth.getInstance()
        //Initialize Firebase Database
        database = FirebaseDatabase.getInstance()

        binding.AddItemButton.setOnClickListener {
            //Get data from edit text
            foodName = binding.enterFoodName.text.toString().trim()
            foodPrice = binding.enterFoodPrice.text.toString().trim()
            foodDescription = binding.description.text.toString().trim()
            foodIngredient = binding.ingredient.text.toString().trim()

            if (!(foodName.isBlank() || foodDescription.isBlank() || foodPrice.isBlank() || foodIngredient.isBlank())) {
                uploadData()
                Toast.makeText(this, "Item Added Successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
            }
        }

        binding.selectImage.setOnClickListener {
            pickImage.launch("image/*")
        }
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun uploadData() {
        //get a reference to the menu node in the database
        val menuRef = database.getReference("Menu")
        //Generate a unique key for the new menu item
        val newItemKey = menuRef.push().key
        if (foodImageUri != null) {
            val storage = FirebaseStorage.getInstance().reference
            val imageRef = storage.child("menu_images/${newItemKey}.jpg")
            val uploadTask = imageRef.putFile(foodImageUri!!)
            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val newItem = AllMenu(
                        foodName, foodPrice, foodDescription, downloadUri.toString(), foodIngredient
                    )
                    newItemKey?.let {
                        menuRef.child(it).setValue(newItem).addOnSuccessListener {
                            Toast.makeText(this, "Item Added Successfully", Toast.LENGTH_SHORT)
                                .show()
                        }.addOnFailureListener {
                            Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show()
                        }
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Image Upload failed", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
        }
    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.selectedImage.setImageURI(uri)
            foodImageUri = uri
        }

    }
}