package com.timmy.codelab.customview

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.timmy.codelab.customview.databinding.ActivityLobbyBinding

class LobbyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLobbyBinding

    private val demoAdapter = DemoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLobbyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        generateDemo()
    }

    private fun initView() {
        binding.demoList.run {
            layoutManager = LinearLayoutManager(context)
            adapter = demoAdapter
        }
        demoAdapter.itemClickBlock = { position: Int, view: View, data: Demo ->
            val action = getString(R.string.action_present)
            val uriProvider = UriProvider(this)
            val uriString = uriProvider.clientUriDomain + data.uriPath
            val intent = Intent(action, Uri.parse(uriString))
            val handleIntentActivities = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            if (handleIntentActivities.size > 0) {
                startActivity(intent)
            }
        }
    }

    private fun generateDemo() {
        val demos = mutableListOf<Demo>().apply {
        }
        demoAdapter.setData(demos)
    }
}