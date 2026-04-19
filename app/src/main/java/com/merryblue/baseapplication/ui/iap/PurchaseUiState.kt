package com.merryblue.baseapplication.ui.iap

import android.content.Context
import android.text.SpannableStringBuilder
import androidx.core.content.res.ResourcesCompat
import com.merryblue.baseapplication.R
import com.merryblue.baseapplication.coredata.model.SubscriptionModel
import com.merryblue.baseapplication.coredata.model.SubscriptionModel.BillingPeriod
import com.merryblue.baseapplication.helpers.append
import org.app.core.base.extensions.getColorR

data class PurchaseUiState(
    val loading: Boolean = true,
    val purchased: BillingPeriod = BillingPeriod.NONE,
    val selected: BillingPeriod = BillingPeriod.P1Y,
    val products: List<SubscriptionModel> = emptyList(),
) {
    fun isYearly() = selected == BillingPeriod.P1Y

    fun isMonthly() = selected == BillingPeriod.P1M

    fun yearlyTrial() : Boolean {
        val hasTrial = products.firstOrNull { it.period == BillingPeriod.P1Y }?.hasTrial != false
        return hasTrial
    }

    fun headerTitle(context: Context) : SpannableStringBuilder {
        return context.getString(R.string.txt_upgrade_to).append(
            " ${context.getString(R.string.txt_premium)}",
            context.getColorR(R.color.colorPink05),
            ResourcesCompat.getFont(context, R.font.roboto_bold)
        )
    }

    fun yearlyTitle(context: Context) : SpannableStringBuilder {
        val typeface = if (selected == BillingPeriod.P1Y) {
            ResourcesCompat.getFont(context, R.font.roboto_bold)
        } else {
            ResourcesCompat.getFont(context, R.font.roboto_regular)
        }
        val prefix = "".append(
            if (purchased == BillingPeriod.P1Y) context.getString(R.string.txt_already_yearly_premium) else context.getString(R.string.txt_premium_yearly_package),
            if (selected == BillingPeriod.P1Y) context.getColorR(R.color.colorWhite) else context.getColorR(R.color.toolbarTitleColor),
            typeface
        )

        val suffix = "".append(
            " (${context.getString(R.string.txt_discount)} 70%)",
            context.getColorR(R.color.colorPrimary),
            ResourcesCompat.getFont(context, R.font.roboto_bold)
        )

        return if (purchased == BillingPeriod.P1Y) prefix else prefix.append(suffix)
    }

    fun monthlyTitle(context: Context) : SpannableStringBuilder {
        val typeface = if (selected == BillingPeriod.P1M) {
            ResourcesCompat.getFont(context, R.font.roboto_bold)
        } else {
            ResourcesCompat.getFont(context, R.font.roboto_regular)
        }
        return "".append(
            if (purchased == BillingPeriod.P1M) context.getString(R.string.txt_already_monthly_premium) else context.getString(R.string.txt_premium_monthly_package),
            if (selected == BillingPeriod.P1M) context.getColorR(R.color.colorWhite) else context.getColorR(R.color.colorWhite),
            typeface
        )
    }

    fun monthlyPrice(context: Context) : SpannableStringBuilder  {
        val price = products.firstOrNull { it.period == BillingPeriod.P1M }?.formatPrice ?: "\$2.99"

        val typeface = if (selected == BillingPeriod.P1M) {
            ResourcesCompat.getFont(context, R.font.roboto_bold)
        } else {
            ResourcesCompat.getFont(context, R.font.roboto_regular)
        }
        return "".append(
            price + "/" + context.getString(R.string.txt_month),
            if (selected == BillingPeriod.P1M) context.getColorR(R.color.colorPrimary) else context.getColorR(R.color.colorWhite),
            typeface
        )
    }

    fun yearlyPrice(context: Context): SpannableStringBuilder {
        val price = products.firstOrNull { it.period == BillingPeriod.P1Y }?.formatPrice ?: "\$21.50"

        val typeface = if (selected == BillingPeriod.P1Y) {
            ResourcesCompat.getFont(context, R.font.roboto_regular)
        } else {
            ResourcesCompat.getFont(context, R.font.roboto_regular)
        }
        return "".append(
            price + "/" + context.getString(R.string.txt_year),
            if (selected == BillingPeriod.P1Y) context.getColorR(R.color.colorPrimary) else context.getColorR(R.color.colorWhite),
            typeface
        ).append(" " + context.getString(R.string.txt_try_free_trial))
    }

    fun isMonthlyEnable() : Boolean {
        val filter = products.firstOrNull { it.period == BillingPeriod.P1M }
        return filter != null
    }

    fun isYearlyEnable() : Boolean {
        val filter = products.firstOrNull { it.period == BillingPeriod.P1Y }
        return filter != null
    }

    fun confirmTitle(context: Context) : String {
        when(purchased) {
            BillingPeriod.NONE -> {
                val currencyCode = products.firstOrNull()?.currencyCode ?: "$"
                return if (selected == BillingPeriod.P1Y) {
                    val hasTrial = products.firstOrNull { it.period == BillingPeriod.P1Y }?.hasTrial != false
                    if (hasTrial) {
                        context.getString(R.string.txt_try_free) + " 0$currencyCode"
                    } else {
                        context.getString(R.string.txt_continue)
                    }
                } else {
                    context.getString(R.string.txt_continue)
                }
            }
            BillingPeriod.P1M -> {
                return context.getString(R.string.txt_already_monthly_premium)
            }
            BillingPeriod.P1Y -> {
                return context.getString(R.string.txt_already_yearly_premium)
            }
            else -> {
                return context.getString(R.string.txt_continue)
            }
        }
    }
}
