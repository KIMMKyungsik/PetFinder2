package org.techtown.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.*
import org.techtown.adapters.PetListAdapter
import org.techtown.datas.Petdata
import org.techtown.petfinder.ReportActivity
import org.techtown.petfinder.databinding.LostPetFragmentBinding

class LostPetFragment : Fragment(), EventListener<QuerySnapshot> {

    private lateinit var binding: LostPetFragmentBinding
    private lateinit var adapter: PetListAdapter

    private lateinit var db: FirebaseFirestore
    private var listenerRegistration: ListenerRegistration? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =LostPetFragmentBinding .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FirebaseFirestore.getInstance()

        with(binding) {
            recyclerView.layoutManager = GridLayoutManager(context, 2)

            adapter = PetListAdapter(root.context)
            recyclerView.adapter = adapter

            regesterButton.setOnClickListener {
                ReportActivity.startActivity(this@LostPetFragment, ReportActivity.Companion.PetListType.LOST)
            }
        }


        listenerRegistration = db.collection("pet")
            .whereEqualTo("type", 0)
            .whereEqualTo("activation", true)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener(this)
    }

    override fun onDestroy() {
        listenerRegistration?.remove()

        super.onDestroy()
    }

    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
        error?.printStackTrace()

        val snapshot = value ?: return
        adapter.submitList(snapshot.documents.map { it.toObject(Petdata::class.java) })
    }
}