import './../style/style.css'
import PropTypes from "prop-types";

const Tag = (props) => {
    return (
        <div className="tag_container">
            <img className="tag_img" src="/img/close.png" alt="" onClick={() => props.onDelete(props.tag.id)}/>
            <p className="tag_name">
                {props.tag.value}
            </p>
        </div>
    );
}

Tag.propTypes = {
    tag: PropTypes.object.isRequired,
    onDelete: PropTypes.func.isRequired
}

export default Tag;