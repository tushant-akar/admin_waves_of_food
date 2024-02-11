package com.example.adminwaveoffoods

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.adminwaveoffoods.databinding.ActivityAdminProfileBinding

class AdminProfileActivity : AppCompatActivity() {
    private val binding : ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.name.isEnabled = false
        binding.address.isEnabled = false
        binding.phone.isEnabled = false
        binding.email.isEnabled = false
        binding.password.isEnabled = false
        var isEnable = false
        binding.edtTextClickable.setOnClickListener {
            isEnable = !isEnable
            binding.name.isEnabled = isEnable
            binding.address.isEnabled = isEnable
            binding.phone.isEnabled = isEnable
            binding.email.isEnabled = isEnable
            binding.password.isEnabled = isEnable
            if(isEnable) {
                binding.name.requestFocus()
            }
        }
    }
}