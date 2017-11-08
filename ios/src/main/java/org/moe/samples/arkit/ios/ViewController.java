// Copyright (c) 2015, Intel Corporation
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are
// met:
//
// 1. Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above
// copyright notice, this list of conditions and the following disclaimer
// in the documentation and/or other materials provided with the
// distribution.
// 3. Neither the name of the copyright holder nor the names of its
// contributors may be used to endorse or promote products derived from
// this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
// A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
// HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
// LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
// THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package org.moe.samples.arkit.ios;

import org.moe.natj.general.Pointer;
import org.moe.natj.general.ann.Owned;
import org.moe.natj.general.ann.RegisterOnStartup;
import org.moe.natj.objc.ObjCRuntime;
import org.moe.natj.objc.ann.IBOutlet;
import org.moe.natj.objc.ann.ObjCClassName;
import org.moe.natj.objc.ann.Selector;

import apple.arkit.*;
import apple.arkit.protocol.ARSessionDelegate;
import apple.foundation.c.Foundation;
import apple.uikit.UIViewController;

@org.moe.natj.general.ann.Runtime(ObjCRuntime.class)
@ObjCClassName("ViewController")
@RegisterOnStartup
public class ViewController extends UIViewController {

  @Selector("sceneView")
  @org.moe.natj.objc.ann.Property
  @IBOutlet
  public native ARSCNView sceneView();

  @Owned
  @Selector("alloc")
  public static native ViewController alloc();

  @Selector("init")
  public native ViewController init();

  private ARSession session;

  protected ViewController(Pointer peer) {
    super(peer);
  }

  @Override
  public void viewDidLoad() {
    super.viewDidLoad();
    log("viewDidLoad");

    session = ARSession.alloc().init();
    session.setDelegate(new ARSessionDelegate() {
      @Override
      public void sessionDidUpdateFrame(ARSession session, ARFrame frame) {
        log("sessionDidUpdateFrame");
      }

      @Override
      public void sessionWasInterrupted(ARSession session) {
        log("sessionWasInterrupted");
      }

      @Override
      public void sessionInterruptionEnded(ARSession session) {
        log("sessionInterruptionEnded");
      }
    });
  }

  @Override
  public void viewWillAppear(boolean animated) {
    super.viewWillAppear(animated);
    log("viewWillAppear");
    log("isSupported = " + ARWorldTrackingConfiguration.isSupported());

    ARWorldTrackingConfiguration configuration = ARWorldTrackingConfiguration.alloc().init();
    session.runWithConfiguration(configuration);
  }

  @Override
  public void viewWillDisappear(boolean animated) {
    super.viewWillDisappear(animated);
    log("viewWillDisappear");
    session.pause();
  }

  private static void log(String s) {
    Foundation.NSLog(s);
  }
}
