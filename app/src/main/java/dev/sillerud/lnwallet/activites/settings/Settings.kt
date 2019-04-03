package dev.sillerud.lnwallet.activites.settings

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceCategory
import android.support.v7.preference.PreferenceFragmentCompat
import dev.sillerud.lnwallet.R
import dev.sillerud.lnwallet.activites.ActivityBase
import dev.sillerud.lnwallet.activites.LightningAwareActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }
}

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val context = preferenceManager.context
        val screen = preferenceManager.createPreferenceScreen(context)

        val walletCategory = PreferenceCategory(context).apply {
            key = "wallet_category"
            title = "Wallets"
        }
        screen.addPreference(walletCategory)

        preferenceManager.sharedPreferences.getStringSet(WalletSettingsActivity.LN_CONNECTION_IDS, mutableSetOf())?.forEach { connectionId ->
            val connectionInfo = preferenceManager.sharedPreferences.getLightningConnectionInfo(connectionId)
            walletCategory.addPreference(Preference(context).apply {
                key = connectionId
                title = connectionInfo.name ?: connectionInfo.host
                summary = "${connectionInfo.host}:${connectionInfo.port}"

                setOnPreferenceClickListener {
                    startActivityForResult(Intent(context, WalletSettingsActivity::class.java).apply {
                        putExtra(ActivityBase.LN_CONNECTION_ID_EXTRA, connectionId)
                        putExtra(ActivityBase.LN_CONNECTION_INFO_EXTRA, connectionInfo)
                    }, 1)

                    true
                }
            })
        }

        walletCategory.addPreference(Preference(context).apply {
            key = "add_wallet"
            title = "Add wallet"
            summary = "Add a new lightning wallet"
            setOnPreferenceClickListener {
                startActivityForResult(Intent(context, WalletSettingsActivity::class.java), 1)

                true
            }
        })

        preferenceScreen = screen
    }
}