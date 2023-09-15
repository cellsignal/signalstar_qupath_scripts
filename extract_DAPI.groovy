import qupath.lib.gui.images.servers.ChannelDisplayTransformServer
import qupath.lib.display.ChannelDisplayInfo

import static qupath.lib.gui.scripting.QPEx.*

String path = buildFilePath(PROJECT_BASE_DIR, 'dapi', getProjectEntry().getImageName() + '.tif')

def currentServer = getCurrentImageData().getServer()

def channels = []
getCurrentViewer().getImageDisplay().availableChannels().each {
    if(it.name.startsWith('DAPI')) {
        channels.add(it)
    }
}
println(channels)

def server =  new ChannelDisplayTransformServer(currentServer, channels as List<ChannelDisplayInfo>)
mkdirs(new File(path).getParent())
writeImage(server, path)
