import './../style/style.css'
import PropTypes from "prop-types";
import {useState} from "react";

const Note = (props) => {
    // const [isSelected, setIsSelected] = useState(false);

    const paddingLeft = (props.note?.folder) ? "25px" : "15px";
    const backgroundColor = (props.isSelected) ? "#f6f6f6" : "#ffffff";

    return (
        <>
            <div draggable={true}
                 onDragOver={(e) => e.preventDefault()}
                 onDragEnd={(e) => e.preventDefault()}
                 onDragStart={() => props.onDragStart(props.note)}
                 onClick={() => {
                     props.onClick(`/${props.note?.id}`);
                 }}
                 className="note"
                 style={{paddingLeft: paddingLeft, backgroundColor: backgroundColor}}>
                <div>
                    <h4 className="note_title">
                        {props.note?.content ? `${props.note?.content.substring(0, 15)}...` : "Type your note..."}
                    </h4>
                    <p className="note_status">
                        {props.note?.status}
                    </p>
                </div>
                <img className="img note_img" src="/img/delete.svg" onClick={() => props.onDelete(props.note?.id)}
                     alt="#"/>
            </div>
        </>

    );
}

Note.propTypes = {
    note: PropTypes.object,
    onDragStart: PropTypes.func.isRequired,
    onDelete: PropTypes.func.isRequired,
    onClick: PropTypes.func.isRequired,
    isSelected: PropTypes.bool.isRequired
}

export default Note;