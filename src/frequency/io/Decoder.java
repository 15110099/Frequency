/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frequency.io;

/**
 *
 * @author luke
 */
public interface Decoder {
    public int readSamples( float[] samples );
}