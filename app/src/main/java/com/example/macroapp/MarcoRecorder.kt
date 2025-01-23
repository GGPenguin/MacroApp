override fun stopRecording() {
    isRecording = false
    saveGesturesToDatabase("New Macro") // Save the macro with a default name
    Log.d("MacroRecorder", "Recording Stopped. Gestures Saved.")
}

private fun saveGesturesToDatabase(name: String) {
    val macro = MacroEntity(name = name, gestures = recordedGestures)

    // Save macro to database using a background thread
    val database = MacroDatabase.getDatabase(applicationContext)
    val macroDao = database.macroDao()
    Thread {
        macroDao.insertMacro(macro)
        Log.d("MacroRecorder", "Macro Saved to Database: $name")
    }.start()
}

fun playbackSavedMacro(macroId: Int) {
    val database = MacroDatabase.getDatabase(applicationContext)
    val macroDao = database.macroDao()

    Thread {
        val macro = macroDao.getMacroById(macroId)
        macro?.let {
            recordedGestures.clear()
            recordedGestures.addAll(it.gestures)
            playbackGestures() // Play the gestures
        }
    }.start()
}
