package com.jobhunter.app.ui.search

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.jobhunter.app.ui.offersDelegate
import com.jobhunter.domain.model.Offer


internal class OffersAdapter(
    onItemClickListener: (Offer) -> Unit
) : ListDelegationAdapter<List<Offer>>(offersDelegate(onItemClickListener)) {

    var offers: List<Offer>
        get() = items ?: emptyList()
        set(newList) {
            val diffUtilResult = DiffUtil.calculateDiff(OffersDiffUtil(offers, newList))
            items = newList
            diffUtilResult.dispatchUpdatesTo(this)
        }
}


private class OffersDiffUtil(
    private val oldList: List<Offer>,
    private val newList: List<Offer>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }
}



