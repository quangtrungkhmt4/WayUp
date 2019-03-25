package com.example.wayupmanagement.model;

import android.graphics.Bitmap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BitmapImage extends AbstractModel{

    private Bitmap bitmap;
}
