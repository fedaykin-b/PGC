/**********************************************
 * Copyright (C) 2011 Lukas Laag
 * This file is part of lib-gwt-file.
 * 
 * libgwtsvg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * libgwtsvg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with libgwtsvg.  If not, see http://www.gnu.org/licenses/
 **********************************************/
package org.vectomatic.arrays;

import com.google.gwt.core.client.JsArrayInteger;

public class Int32Array extends ArrayBufferView {
  public static final int BYTES_PER_ELEMENT = 4;
  protected Int32Array() {
  }

  public static final native Int32Array createInt32Array(int length) /*-{
    return new Int32Array(length);
  }-*/;
  public static final native Int32Array createInt32Array(Int32Array array) /*-{
    return new Int32Array(array);
  }-*/;
  public static final native Int32Array createInt32Array(JsArrayInteger array) /*-{
    return new Int32Array(array);
  }-*/;
  public static final native Int32Array createInt32Array(ArrayBuffer buffer) /*-{
    return new Int32Array(buffer);
  }-*/;
  public static final native Int32Array createInt32Array(ArrayBuffer buffer, int byteOffset) /*-{
    return new Int32Array(buffer, byteOffset);
  }-*/;
  public static final native Int32Array createInt32Array(ArrayBuffer buffer, int byteOffset, int length) /*-{
    return new Int32Array(buffer, byteOffset, length);
  }-*/;
  // Implementation of the typedarray::Int32Array W3C IDL interface
  public final native int getLength() /*-{
    return this.length;
  }-*/;
  public final native int get(int index) /*-{
    return this.get(index);
  }-*/;
  public final native void set(int index, int value) /*-{
    this.set(index, value);
  }-*/;
  public final native void set(Int32Array array, int offset) /*-{
    this.set(array, offset);
  }-*/;
  public final native void set(JsArrayInteger array, int offset) /*-{
    this.set(array, offset);
  }-*/;
  public final native Int32Array subarray(int start, int end) /*-{
    return this.subarray(start, end);
  }-*/;

}
