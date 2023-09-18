import qupath.opencv.ops.ImageOps

import static qupath.lib.gui.scripting.QPEx.*
import qupath.ext.biop.cellpose.Cellpose2D

// Specify the model name (cyto, nuclei, cyto2, omni_bact or a path to your custom model)
def pathModel = 'nuclei'
//def pathModel = 'c:/Users/Administrator/Documents/multiplex/jared_burke_panel_1/cellpose_train/models/lr002_v2.zip'
//def pathModel = 'c:/Users/Administrator/Documents/multiplex/dsDNAse_panel2/dsDNAse_panel2_train/models/lr002.zip'

def cellpose= Cellpose2D.builder( pathModel )
        .pixelSize( 0.5 )              // Resolution for detection
        .channels( 'day_1-DAPI')            // Select detection channel(s)
//        .preprocess(
//                ImageOps.Core.subtract(35.0),
//                ImageOps.Core.clip(0.0, 438)
//                ImageOps.Filters.median(1)
//        )                // List of preprocessing ImageOps to run on the images before exporting them
//        .normalizePercentilesGlobal(0.1, 99.8, 10) // Convenience global percentile normalization. arguments are percentileMin, percentileMax, dowsample.
        .tileSize(2048)                // If your GPU can take it, make larger tiles to process fewer of them. Useful for Omnipose
//        .cellposeChannels(1,2)         // Overwrites the logic of this plugin with these two values. These will be sent directly to --chan and --chan2
//        .cellprobThreshold(0.0)        // Threshold for the mask detection, defaults to 0.0
//        .flowThreshold(0.4)            // Threshold for the flows, defaults to 0.4
        .diameter(0)                   // Median object diameter. Set to 0.0 for the `bact_omni` model or for automatic computation
//        .addParameter("save_flows")      // Any parameter from cellpose not available in the builder. See https://cellpose.readthedocs.io/en/latest/command.html
//        .addParameter("anisotropy", "3") // Any parameter from cellpose not available in the builder. See https://cellpose.readthedocs.io/en/latest/command.html
        .setOverlap(100)  // tip: set roughly to two max diameters (in pixels)
//        .invert()                      // Have cellpose invert the image
        .cellExpansion(5.0)            // Approximate cells based upon nucleus expansion
        .cellConstrainScale(1.5)       // Constrain cell expansion using nucleus size
//        .classify("My Detections")     // PathClass to give newly created objects
        .measureShape()                // Add shape measurements
        .measureIntensity()            // Add cell measurements (in all compartments)
//        .createAnnotations()           // Make annotations instead of detections. This ignores cellExpansion
        .build()

// Run detection for the selected objects
def imageData = getCurrentImageData()
def pathObjects = getSelectedObjects()
if (pathObjects.isEmpty()) {
    Dialogs.showErrorMessage("Cellpose", "Please select a parent object!")
    return
}
cellpose.detectObjects(imageData, pathObjects)
println 'Done!'
