/*
 * SPDX-FileCopyrightText: 2020, microG Project Team
 * SPDX-License-Identifier: Apache-2.0
 */

package org.microg.gms.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.R
import com.google.android.gms.databinding.PushNotificationAppFragmentBinding


class PushNotificationAppFragment : Fragment(R.layout.push_notification_fragment) {
    lateinit var binding: PushNotificationAppFragmentBinding
    val packageName: String?
        get() = arguments?.getString("package")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = PushNotificationAppFragmentBinding.inflate(inflater, container, false)
        binding.callbacks = object : PushNotificationAppFragmentCallbacks {
            override fun onAppClicked() {
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri: Uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                context!!.startActivity(intent)
            }
        }
        childFragmentManager.findFragmentById(R.id.sub_preferences)?.arguments = arguments
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenResumed {
            val pm = requireContext().packageManager
            val applicationInfo = packageName?.let { pm.getApplicationInfo(it, 0) }
            binding.appName = applicationInfo?.loadLabel(pm)?.toString() ?: packageName
            binding.appIcon = applicationInfo?.loadIcon(pm)
                    ?: AppCompatResources.getDrawable(requireContext(), android.R.mipmap.sym_def_app_icon)
        }
    }
}

interface PushNotificationAppFragmentCallbacks {
    fun onAppClicked()
}