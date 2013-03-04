/* -*- mode: jde; c-basic-offset: 2; indent-tabs-mode: nil -*- */
/*
 TargetPackage - Represents a hardware package
 Part of the Arduino project - http://www.arduino.cc/

 Copyright (c) 2011 Cristian Maglie

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software Foundation,
 Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 $Id$
 */
package processing.app.debug;

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import processing.app.helpers.filefilters.OnlyDirs;

public class TargetPackage {

  private String id;

  Map<String, TargetPlatform> platforms = new LinkedHashMap<String, TargetPlatform>();

  public TargetPackage(String _id, File _folder) throws TargetPlatformException {
    id = _id;

    File[] folders = _folder.listFiles(new OnlyDirs());
    if (folders == null)
      return;

    for (File subFolder : folders) {
      if (!subFolder.exists() || !subFolder.canRead())
        continue;
      String arch = subFolder.getName();
      TargetPlatform platform = new TargetPlatform(arch, subFolder, this);
      platforms.put(arch, platform);
    }
  }

  public Map<String, TargetPlatform> getPlatforms() {
    return platforms;
  }

  public Collection<TargetPlatform> platforms() {
    return platforms.values();
  }

  public TargetPlatform get(String platform) {
    return platforms.get(platform);
  }

  public void resolveReferencedPlatforms(Map<String, TargetPackage> packages)
      throws Exception {
    for (TargetPlatform platform : getPlatforms().values())
      platform.resolveReferencedPlatforms(packages);
  }

  public String getId() {
    return id;
  }
}
