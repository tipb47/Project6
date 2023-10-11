package com.example.project6

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.project6.databinding.FragmentNotesBinding

class notesFragment : Fragment() {
    private val TAG = "notesFragment"
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        //binding
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        val view = binding.root


        val application = requireNotNull(this.activity).application
        val dao = NoteDatabase.getInstance(application).noteDao()

        //connect viewModel
        val viewModelFactory = NotesViewModelFactory(dao) // Create a ViewModel factory for NotesViewModel
        val viewModel = ViewModelProvider(this, viewModelFactory)[NotesViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        fun noteClicked (noteId : Long) { //note clicked (to edit)
            viewModel.onNoteClicked(noteId)
        }

        fun yesPressed (noteId : Long) {
            binding.viewModel?.deleteNote(noteId)
        }

        fun deleteClicked (noteId : Long) {
            NoteDialogFragment(noteId,::yesPressed).show(childFragmentManager,
                NoteDialogFragment.TAG)
        }

        val adapter = NoteItemAdapter(::noteClicked, ::deleteClicked) // Replace with your adapter

        binding.notesList.adapter = adapter

        viewModel.notes.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }

        viewModel.navigateToNote.observe(viewLifecycleOwner) { navigationPair ->
            val shouldNavigate = navigationPair.first //triggered if function(s) hit
            val noteId = navigationPair.second

            if (shouldNavigate) {
                if (noteId == null) { //add new note
                    val action = notesFragmentDirections.actionNotesFragmentToEditNoteFragment(-1L) //placeholder, new note.
                    this.findNavController().navigate(action)
                } else { //edit existing note
                    val action = notesFragmentDirections.actionNotesFragmentToEditNoteFragment(noteId) //edit note
                    this.findNavController().navigate(action)
                }
                viewModel.onNoteNavigated()
            }
        }

        viewModel.logAllNotes()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}