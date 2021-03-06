# To access the camera and X server of the host system (Ubuntu), run the image built from this dockerfile as:
# >> xhost +
# >> docker run -it -e DISPLAY=$DISPLAY -e NO_AT_BRIDGE=1 -e JAVA_HOME=/root/jdk1.8.0_181 \
#               --privileged --device /dev/video0 \
#               --privileged -v /tmp/.X11-unix:/tmp/.X11-unix:ro yourImageName /bin/bash
#
# Upon completion, do the following:
# >> xhost -

FROM python:3.6-slim-stretch

RUN apt-get update && apt-get install -y \
    build-essential \
    cmake \
    pkg-config \
    libjpeg-dev \
    libpng-dev \
    libtiff-dev \
    libavcodec-dev \
    libavformat-dev \
    libswscale-dev \
    libv4l-dev \
    libxvidcore-dev \
    libx264-dev \
    libgtk-3-dev \
    libcanberra-gtk3-module \
    libgstreamer1.0-dev \
    libgstreamer-plugins-base1.0-dev \
    libatlas-base-dev \
    gfortran \
    python3-dev \
    wget \
    zip \
    git

RUN cd ~ && \
    wget -O opencv.zip https://github.com/opencv/opencv/archive/3.4.2.zip && \
    wget -O opencv_contrib.zip https://github.com/opencv/opencv_contrib/archive/3.4.2.zip && \
    unzip opencv.zip && \
    unzip opencv_contrib.zip && \
    pip install numpy && \
    cd ~/opencv-3.4.2 && \
    mkdir build && \
    cd build && \
    cmake -D CMAKE_BUILD_TYPE=RELEASE \
        -D CMAKE_INSTALL_PREFIX=/usr/local/ \
        -D INSTALL_PYTHON_EXAMPLES=OFF \
        -D INSTALL_C_EXAMPLES=OFF \
        -D OPENCV_EXTRA_MODULES_PATH=~/opencv_contrib-3.4.2/modules/ \
        -D PYTHON_EXECUTABLE=/usr/local/bin/python \
        -D BUILD_EXAMPLES=OFF \
        -D WITH_V4L=ON \
        -D WITH_GTK=ON \
        -D WITH_TIFF=ON .. && \
    make -j4 && \
    make install && \
    ldconfig && \
    cd /usr/local/lib/python3.6/site-packages/ && \
    mv cv2.cpython-36m-x86_64-linux-gnu.so cv2.so && \
    cd ~ && \
    rm opencv.zip opencv_contrib.zip && \
    rm -rf opencv-3.4.2 && \
    rm -rf opencv_contrib-3.4.2	

RUN git clone https://github.com/davisking/dlib.git && \
    cd dlib && \
    mkdir build && \
    cd build && \
    cmake .. -DUSE_AVX_INSTRUCTIONS=1 && \
    cmake --build . && \
    cd .. && \
    python setup.py install --yes USE_AVX_INSTRUCTIONS && \
    pip install face_recognition && \
    pip install imutils && \
    pip install imageio

# NOTE: Oracle regularly change the versions available for download - so update the below appropriately.
#       See https://github.com/dockerfile/java/issues/36
RUN \
    JAVA_VERSION_MAJOR=8 \
    JAVA_VERSION_MINOR=191 \
    JAVA_VERSION_BUILD=12 \
    JAVA_DOWNLOAD_HASH=2787e4a523244c269598db4e85c51e0c && \
    cd ~ && \
    wget --no-check-certificate -c --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/${JAVA_VERSION_MAJOR}u${JAVA_VERSION_MINOR}-b${JAVA_VERSION_BUILD}/${JAVA_DOWNLOAD_HASH}/jdk-${JAVA_VERSION_MAJOR}u${JAVA_VERSION_MINOR}-linux-x64.tar.gz && \
    tar -xzf jdk-${JAVA_VERSION_MAJOR}u${JAVA_VERSION_MINOR}-linux-x64.tar.gz

COPY ./ /home/face-recognition-demo/
