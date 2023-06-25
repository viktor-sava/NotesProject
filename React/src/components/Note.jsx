import './../style/style.css'
import PropTypes from "prop-types";

const Note = (props) => {
    return (
        <>
            <div draggable={true}
                 onDragOver={(e) => e.preventDefault()}
                 onDragEnd={(e) => e.preventDefault()}
                 onDragStart={() => props.onDragStart(props.note)}
                 onClick={() => props.onClick(`/${props.note?.id}`)}
                 className="note"
                 style={(props.note?.folder) ? {paddingLeft: "25px"} : {paddingLeft: "15px"}}>
                <div>
                    <h4 className="note_title">
                        {props.note?.content ? `${props.note?.content.substring(0, 15)}...` : "Type your note..."}
                    </h4>
                    <p className="note_status">
                        {props.note?.status}
                    </p>
                </div>
                <img className="img note_img" src="/img/delete.svg" onClick={() => props.onDelete(props.note?.id)} alt="#"/>
            </div>
        </>

    );
}

Note.propTypes = {
    note: PropTypes.object,
    onDragStart: PropTypes.func.isRequired,
    onDelete: PropTypes.func.isRequired,
    onClick: PropTypes.func.isRequired
}

export default Note;