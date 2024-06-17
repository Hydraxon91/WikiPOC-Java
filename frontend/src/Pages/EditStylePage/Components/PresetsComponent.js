import PresetCardComponent from "./PresetCardComponent";

const PresetsComponent = ({handleChange, logo}) =>{
    const stylesArray = [
        {
            bodyColor: "rgb(80, 124, 237)",
            articleColor: "rgb(82, 108, 173)",
            articleRightColor: "rgb(60, 195, 184)",
            articleRightInnerColor: "rgb(43, 78, 237)",
            footerListTextColor: "rgb(38, 58, 113)",
            footerListLinkTextColor: "rgb(29, 48, 94)",
            fontFamily: "Times New Roman"
        },
        {
            bodyColor: "rgb(255, 15, 51)",
            articleColor: "rgb(229, 91, 31)",
            articleRightColor: "rgb(220, 9, 9)",
            articleRightInnerColor: "rgb(223, 133, 7)",
            footerListTextColor: "rgb(242, 235, 7)",
            footerListLinkTextColor: "rgb(242, 235, 7)",
            fontFamily: "Arial"
        },
        {
            bodyColor: "rgb(128, 0, 128)", // Purple
            articleColor: "rgb(186, 85, 211)", // Medium Purple
            articleRightColor: "rgb(147, 112, 219)", // Medium Purple
            articleRightInnerColor: "rgb(138, 43, 226)", // Blue Violet
            footerListTextColor: "rgb(72, 61, 139)", // Dark Slate Blue
            footerListLinkTextColor: "rgb(75, 0, 130)", // Indigo
            fontFamily: "Comic Sans MS"
        },
        {
            bodyColor: "rgb(134, 96, 67)", // Dirt Brown
            articleColor: "rgb(160, 120, 94)", // Lighter Dirt Brown
            articleRightColor: "rgb(165, 126, 82)", // Even Lighter Dirt Brown
            articleRightInnerColor: "rgb(98, 167, 79)", // Green Grass
            footerListTextColor: "rgb(94, 60, 42)", // Darker Dirt Brown
            footerListLinkTextColor: "rgb(74, 44, 28)", // Darkest Dirt Brown
            footerListLinkTextColor: "rgb(98, 167, 79)", // Green Grass
            fontFamily: "Times New Roman"
        },
        {
            bodyColor: "rgb(255, 255, 255)", // White
            articleColor: "rgb(245, 245, 245)", // Light Gray
            articleRightColor: "rgb(255, 255, 255)", // White
            articleRightInnerColor: "rgb(240, 240, 240)", // Lighter Gray
            footerListTextColor: "rgb(70, 70, 70)", // Dark Gray
            footerListLinkTextColor: "rgb(0, 120, 231)", // Wikipedia Blue
            fontFamily: "Helvetica, Arial, sans-serif" // Standard web font
        }     
        // Add more objects as needed
      ];

    return(
         <div className="wikipage-preset-component">
            {stylesArray.map((stylePreset, index) => (
                <PresetCardComponent key={index} stylePreset={stylePreset} logo={logo} handleChange={handleChange}/>
            ))}
        </div>
    )
}

export default PresetsComponent;