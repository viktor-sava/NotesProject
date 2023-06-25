import {useState} from "react";
import './../style/style.css'
import Note from "./Note.jsx";
import PropTypes from "prop-types";

const Folder = (props) => {
    const [isHidden, setIsHidden] = useState(true);
    const onHandleClick = () => {
        setIsHidden(!isHidden);
    };

    return (
        <>
            <div className="folder"
                 onClick={onHandleClick}
                 onDragEnd={(e) => e.preventDefault()}
                 onDragOver={(e) => e.preventDefault()}
                 onDrop={(e) => {
                     e.preventDefault();
                     props.onDrop(props.folder);
                 }}>
                <h4 className="folder_title">
                    {props.folder.name}
                </h4>
                {(isHidden) ? (<img className="img" src="/img/keyboard_arrow_right.svg" alt="#"/>) : (<img className="img" src="/img/keyboard_arrow_down.svg" alt="#"/>)}
            </div>
            <div className="note_container" style={(isHidden) ? {display: "none"} : {display: "block"}}>
                {props.children}
            </div>
        </>
    );
}

Folder.propTypes = {
    children: PropTypes.arrayOf(Note),
    onDrop: PropTypes.func.isRequired,
    folder: PropTypes.object.isRequired
}

export default Folder;