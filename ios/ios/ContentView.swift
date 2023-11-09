import UIKit
import SwiftUI
import shared
import GoogleMaps

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {

       // Load the Google maps API key from the AppSecrets.plist file
       let filePath = Bundle.main.path(forResource: "AppSecrets", ofType: "plist")!
       let plist = NSDictionary(contentsOfFile: filePath)!
       let googleMapsApiKey = plist["GOOGLE_MAPS_API_KEY"] as! String

        GMSServices.provideAPIKey(googleMapsApiKey)
        GMSServices.setMetalRendererEnabled(true)
        return Main_iosKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
               .ignoresSafeArea() // Compose has own keyboard handler
    }
}
