/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.platform

import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene
import platform.UIKit.popoverPresentationController

class IosShareService : ShareService {
  override fun share(text: String) {
    val presenter = topViewController() ?: return
    val controller =
      UIActivityViewController(activityItems = listOf(text), applicationActivities = null)
    // iPad shows the share sheet as a popover and needs an anchor.
    controller.popoverPresentationController?.sourceView = presenter.view
    presenter.presentViewController(controller, animated = true, completion = null)
  }

  private fun topViewController(): UIViewController? {
    val scene =
      UIApplication.sharedApplication.connectedScenes
        .filterIsInstance<UIWindowScene>()
        .firstOrNull() ?: return null
    val window =
      scene.windows.filterIsInstance<UIWindow>().firstOrNull { it.isKeyWindow() }
        ?: scene.windows.filterIsInstance<UIWindow>().firstOrNull()
    var controller = window?.rootViewController
    while (controller?.presentedViewController != null) {
      controller = controller.presentedViewController
    }
    return controller
  }
}
