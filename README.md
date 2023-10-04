# Mandelbrot Set Renderer

## Introduction

The Mandelbrot set is a fascinating mathematical set of complex numbers defined by a simple recursive formula. This project focuses on rendering the Mandelbrot set using the escape-time algorithm and presents three modes of implementation: sequential, parallel (multi-threaded), and distributed (RMI - Remote Method Invocation).

## Implementation

### Sequential Mode

In the sequential mode, the program uses a single thread to draw the Mandelbrot set on the canvas. Each pixel is processed sequentially, making it the most basic implementation.

### Parallel Mode (Multi-threaded)

The parallel mode optimizes the rendering process by dividing the work among multiple "worker" threads. These threads run in parallel, independently processing different parts of the canvas. After completing their tasks, they send back their chunks to the main thread, which reconstructs the final image.

### Distributed Mode (RMI)

The distributed mode leverages Java's Remote Method Invocation (RMI) to achieve remote method execution. While methods are executed sequentially like in the sequential mode, RMI allows for the illusion of calling local methods, making it suitable for distributed computing.

## Technical Details

- Implemented in JavaFX with the help of JavaFX Scene Builder for the graphical interface.
- RMI implementation includes a remote interface, an implementation of the remote interface, a server class, and a client class.
- Each method in the remote interface must throw a `java.rmi.RemoteException`.

## Testing

Extensive testing was performed to measure the time required for rendering at different resolutions. The results indicated that as the height and width of the canvas increase, rendering time also increases. The sequential mode was the fastest, while the parallel mode took the most time, placing the distributed mode in the middle.

## Conclusion

This project showcases various methods for rendering the Mandelbrot set on a canvas at high resolutions. It provides insights into the advantages and trade-offs of sequential, parallel, and distributed implementations.

## Usage

To use this program:

1. Build and run the application.
2. Choose the mode (sequential, parallel, or distributed) based on your requirements.
3. Specify the canvas size.
4. Render the Mandelbrot set, and the resulting image will be saved as a PNG or JPEG file.

Enjoy exploring the fascinating world of the Mandelbrot set!


