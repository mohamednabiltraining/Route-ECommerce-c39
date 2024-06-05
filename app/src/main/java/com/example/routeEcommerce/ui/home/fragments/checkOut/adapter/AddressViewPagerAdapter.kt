package com.example.routeEcommerce.ui.home.fragments.checkOut.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.routeEcommerce.databinding.ItemAddressHBinding
import com.example.routeEcommerce.utils.MyDiffUtil
import com.route.domain.models.Address

class AddressViewPagerAdapter : RecyclerView.Adapter<AddressViewPagerAdapter.AddressViewHolder>() {
    private var addressesList: MutableList<Address?> = mutableListOf()

    class AddressViewHolder(val binding: ItemAddressHBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            address: Address?,
            isSelected: Boolean,
        ) {
            address?.let {
                binding.address = it
                binding.isSelected = isSelected
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AddressViewHolder {
        val binding =
            ItemAddressHBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return AddressViewHolder(binding)
    }

    override fun getItemCount(): Int = addressesList.size

    var selectedPosition = 0

    override fun onBindViewHolder(
        holder: AddressViewHolder,
        position: Int,
    ) {
        val address = addressesList[position]
        holder.bind(address, selectedPosition == position)
        /*onAddressCheck?.let { callBack ->
            holder.itemView.setOnClickListener {
                if (address != null) {
                    notifyItemChanged(selectedPosition)
                    selectedPosition = position
                    notifyItemChanged(position)
                    // callBack.invoke(address)
                }
            }
        }*/
        holder.itemView.setOnClickListener {
            if (address != null) {
                notifyItemChanged(selectedPosition)
                selectedPosition = position
                notifyItemChanged(position)
                // callBack.invoke(address)
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

    // / var onAddressCheck: ((address: Address) -> Unit)? = null
}
