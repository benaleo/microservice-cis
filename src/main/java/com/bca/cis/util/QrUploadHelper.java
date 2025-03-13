package com.bca.cis.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class QrUploadHelper {

    public static byte[] generateNewQRCodeWithAvatar(String text, byte[] avatar) throws WriterException, IOException {
        // Generate QR code
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 500, 500);

        // Create an empty BufferedImage for QR code with white background
        BufferedImage qrImage = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = qrImage.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.setStroke(new BasicStroke(2));
        graphics.fillRect(0, 0, 500, 500);

        // Draw the QR code from the BitMatrix
        graphics.setColor(Color.BLACK);
        for (int x = 0; x < 500; x++) {
            for (int y = 0; y < 500; y++) {
                if (bitMatrix.get(x, y)) {
                    graphics.fillRect(x, y, 1, 1);
                }
            }
        }

        // Load avatar image from byte array
        BufferedImage avatarImage = ImageIO.read(new ByteArrayInputStream(avatar));

        // Scale avatar image to fit inside the QR code
        int avatarSize = 50; // Size of the avatar
        int avatarX = (qrImage.getWidth() - avatarSize) / 2; // Center X
        int avatarY = (qrImage.getHeight() - avatarSize) / 2; // Center Y

        // Create a new BufferedImage for the rounded avatar
        BufferedImage roundedAvatar = new BufferedImage(avatarSize, avatarSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D avatarGraphics = roundedAvatar.createGraphics();

        // Enable anti-aliasing
        avatarGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        avatarGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Fill the background with white
        avatarGraphics.setColor(Color.WHITE);
        avatarGraphics.fillOval(0, 0, avatarSize, avatarSize);

        // Clip the avatar to a circular shape
        avatarGraphics.setClip(new java.awt.geom.Ellipse2D.Double(0, 0, avatarSize, avatarSize));
        avatarGraphics.drawImage(avatarImage.getScaledInstance(avatarSize, avatarSize, Image.SCALE_SMOOTH), 0, 0, null);
        avatarGraphics.dispose();

        // Overlay the rounded avatar with a white background onto the QR code
        graphics.drawImage(roundedAvatar, avatarX, avatarY, null);

        // Finalize the graphics and convert to byte array
        graphics.dispose();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "png", baos);

        return baos.toByteArray();
    }


    public String readQRCode(byte[] imageBytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
        BufferedImage bufferedImage = ImageIO.read(bais);

        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        HybridBinarizer binarizer = new HybridBinarizer(source);
        BinaryBitmap bitmap = new BinaryBitmap(binarizer);
        MultiFormatReader reader = new MultiFormatReader();
        try {
            Result result = reader.decode(bitmap);
            return result.getText();
        } catch (ReaderException e) {
            return "Error reading QR Code";
        }

    }
}
