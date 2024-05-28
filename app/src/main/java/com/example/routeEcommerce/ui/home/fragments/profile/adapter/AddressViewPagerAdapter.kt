package com.example.routeEcommerce.ui.home.fragments.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.routeEcommerce.databinding.ItemAddressBinding
import com.example.routeEcommerce.utils.MyDiffUtil
import com.example.routeEcommerce.utils.formatNumber
import com.route.domain.models.Address

class AddressViewPagerAdapter : RecyclerView.Adapter<AddressViewPagerAdapter.AddressViewHolder>() {
    private var addressesList: MutableList<Address?> = mutableListOf()

    class AddressViewHolder(val binding: ItemAddressBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(address: Address?) {
            address?.let {
                binding.address = it
                val phoneNum =
                    it.phone?.formatNumber()
                binding.phoneNumText.text = "+20 $phoneNum"
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AddressViewHolder {
        val binding =
            ItemAddressBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return AddressViewHolder(binding)
    }

    override fun getItemCount(): Int = addressesList.size

    override fun onBindViewHolder(
        holder: AddressViewHolder,
        position: Int,
    ) {
        val address = addressesList[position]
        holder.bind(address)
        deleteAddress?.let { callBack ->
            holder.binding.deleteAddressFab.setOnClickListener {
                address?.let {
                    callBack.invoke(it)
                }
            }
        }
    }

    fun bindCategories(newAddressesList: List<Address?>) {
        val diffUtil =
            MyDiffUtil(addressesList, newAddressesList) { oldItem, newItem ->
                when {
                    oldItem?.id != newItem?.id -> false
                    oldItem?.name != newItem?.name -> false
                    oldItem?.details != newItem?.details -> false
                    oldItem?.city != newItem?.city -> false
                    oldItem?.phone != newItem?.phone -> false
                    else -> true
                }
            }
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        addressesList = newAddressesList.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }

    var deleteAddress: ((address: Address) -> Unit)? = null
}
