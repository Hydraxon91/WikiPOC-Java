const UserImagesContainer = ({ images, insertImage }) => {
    return (
        <>
            {images.length > 0 && (
                <div className='editor-image-container'>
                    {images.map((image, index) => (
                        <div key={index} className="image-preview" onClick={()=> insertImage(image.name)}>
                            <img src={image.dataURL} alt={`Image ${index}`} />
                            <div>{image.name}</div>
                        </div>
                    ))}
                </div>
            )}
        </>
    );
};

export default UserImagesContainer;